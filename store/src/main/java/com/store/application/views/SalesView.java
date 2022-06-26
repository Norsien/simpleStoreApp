package com.store.application.views;

import org.springframework.beans.factory.annotation.Autowired;

import com.store.application.content.product.Product;
import com.store.application.content.product.ProductService;
import com.store.application.content.receipt.Receipt;
import com.store.application.content.receipt.ReceiptService;
import com.store.application.content.sale.Sale;
import com.store.application.content.sale.SaleDTO;
import com.store.application.content.sale.SaleService;
import com.store.application.content.stock.Stock;
import com.store.application.content.stock.StockService;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.ComponentEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.NumberField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.shared.Registration;

@PageTitle("Store | Sales")
@Route(value = "sales", layout = MainLayout.class)
public class SalesView extends VerticalLayout {

    TextField filterText = new TextField();
    Grid<Sale> grid = new Grid<>();
    private SaleService saleService;
    private ProductService productService;
    private ReceiptService receiptService;
    @Autowired
    private StockService stockService;

    public SalesView(SaleService saleService,
                        ProductService productService,
                        ReceiptService receiptService,
                        StockService stockService) {
        this.saleService = saleService;
        this.productService = productService;
        this.receiptService = receiptService;
        addClassName("sale-table-view");
        setSizeFull();

        configureGrid();
        
        add(
            getContent(),
            getBottomBar()
        );

        updateList();
    }
    
    private void configureGrid() {
        grid.addClassName("sale-grid");
        grid.setSizeFull();
        grid.addColumn(p -> p.getProduct().getName()).setHeader("Product").setSortable(true);
        grid.addColumn(p -> p.getReceipt().getCode()).setHeader("Receipt").setSortable(true);
        grid.addColumn(p -> p.getUnits()).setHeader("Units").setSortable(true);
        grid.addColumn(p -> p.getBrutto()).setHeader("Brutto").setSortable(true);
        grid.addColumn(p -> p.getNetto()).setHeader("Netto").setSortable(true);
        grid.addColumn(p -> p.getVatpercentage()).setHeader("VAT %").setSortable(true);
        grid.addColumn(p -> p.getBruttosum()).setHeader("Brutto").setSortable(true);
        grid.addColumn(p -> p.getNettosum()).setHeader("Netto").setSortable(true);
        grid.addColumn(p -> p.getVatsum()).setHeader("VAT").setSortable(true);
        grid.getColumns().forEach(col -> col.setAutoWidth(true));
    }

    private Component getContent() {
        VerticalLayout content = new VerticalLayout(grid);
        content.addClassName("content");
        content.setSizeFull();
        return content;
    }

    private void updateList() {
        grid.setItems(saleService.getSales(filterText.getValue()));
    }

    private Component getBottomBar() {
        TextField productCode = new TextField();
        productCode.setPlaceholder("Product Code");
        TextField receiptCode = new TextField();
        receiptCode.setPlaceholder("Receipt Code");
        NumberField units = new NumberField();
        units.setPlaceholder("Units");
        NumberField netto = new NumberField();
        netto.setPlaceholder("Netto");
        NumberField brutto = new NumberField();
        brutto.setPlaceholder("Brutto");
        NumberField vatpercenntage = new NumberField();
        vatpercenntage.setPlaceholder("VAT %");
        Button addButton = new Button("Create new");

        addButton.addClickListener(e -> validateAndSave(new SaleDTO(productCode.getValue(),
                                                                     receiptCode.getValue(),
                                                                       units.getValue(),
                                                                       netto.getValue(),
                                                                       brutto.getValue(),
                                                                       vatpercenntage.getValue())));

        HorizontalLayout bottomBar = new HorizontalLayout(productCode, receiptCode, units, netto, brutto, vatpercenntage, addButton);
        bottomBar.addClassName("bottom-bar");
        bottomBar.setHeight("3em");
        addListener(SalesView.SaveEvent.class, this::save);
        return bottomBar;
    }

    private void validateAndSave(SaleDTO p) {
        try {
            Sale sale = p.toSale();
            Product product = productService.getProduct(p.productCode);
            Stock stock = product.getStock();
            stock.setUnits(stock.getUnits().subtract(sale.getUnits()));
            sale.setProduct(product);
            Receipt receipt = receiptService.getReceipt(p.receiptCode);
            receipt.setNetto(receipt.getNetto().subtract(sale.getNettosum()));
            receipt.setBrutto(receipt.getBrutto().subtract(sale.getBruttosum()));
            receipt.setVat(receipt.getVat().subtract(sale.getVatsum()));
            sale.setReceipt(receipt);
            fireEvent(new SaveEvent(this, sale, receipt, stock));
        } catch (Exception e) {
            e.printStackTrace();
            ErrorNotification.errorNotification("Cannot create sale.");
        }
    }

    private void save(SalesView.SaveEvent event) {
        saleService.save(event.getSale());
        stockService.save(event.getStock());
        receiptService.save(event.getReceipt());
        updateList();
    }

    public static abstract class SalesEvent extends ComponentEvent<SalesView> {
        private Sale sale;
        private Receipt receipt;
        private Stock stock;

        protected SalesEvent(SalesView source, Sale sale, Receipt receipt, Stock stock) {
            super(source, false);
            this.sale = sale;
            this.receipt = receipt;
            this.stock = stock;
        }

        public Sale getSale() {
            return sale;
        }

        public Receipt getReceipt() {
            return receipt;
        }

        public Stock getStock() {
            return stock;
        }
    }

    public static class SaveEvent extends SalesEvent {
        SaveEvent(SalesView source, Sale sale, Receipt receipt, Stock stock) {
            super(source, sale, receipt, stock);
        }
    }

    public <T extends ComponentEvent<?>> Registration addListener(Class<T> eventType,
                                                                  ComponentEventListener<T> listener) {
        return getEventBus().addListener(eventType, listener);
    }
}