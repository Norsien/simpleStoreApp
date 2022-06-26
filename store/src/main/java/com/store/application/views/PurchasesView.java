package com.store.application.views;

import org.springframework.beans.factory.annotation.Autowired;

import com.store.application.content.delivery.Delivery;
import com.store.application.content.delivery.DeliveryService;
import com.store.application.content.product.Product;
import com.store.application.content.product.ProductService;
import com.store.application.content.purchase.Purchase;
import com.store.application.content.purchase.PurchaseDTO;
import com.store.application.content.purchase.PurchaseService;
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

@PageTitle("Store | Purchases")
@Route(value = "purchases", layout = MainLayout.class)
public class PurchasesView extends VerticalLayout {

    TextField filterText = new TextField();
    Grid<Purchase> grid = new Grid<>();
    private PurchaseService purchaseService;
    private ProductService productService;
    private DeliveryService deliveryService;
    @Autowired
    private StockService stockService;

    public PurchasesView(PurchaseService purchaseService,
                            ProductService productService,
                            DeliveryService deliveryService,
                            StockService stockService) {
        this.purchaseService = purchaseService;
        this.productService = productService;
        this.deliveryService = deliveryService;
        addClassName("purchase-table-view");
        setSizeFull();

        configureGrid();
        
        add(
            getContent(),
            getBottomBar()
        );

        updateList();
    }
    
    private void configureGrid() {
        grid.addClassName("purchase-grid");
        grid.setSizeFull();
        grid.addColumn(p -> p.getProduct().getName()).setHeader("Product").setSortable(true);
        grid.addColumn(p -> p.getDelivery().getCode()).setHeader("Delivery code").setSortable(true);
        grid.addColumn(p -> p.getUnits()).setHeader("Units").setSortable(true);
        grid.addColumn(p -> p.getNettoprice()).setHeader("Netto").setSortable(true);
        grid.addColumn(p -> p.getNettosum()).setHeader("Netto sum").setSortable(true);
        grid.getColumns().forEach(col -> col.setAutoWidth(true));
    }

    private Component getContent() {
        VerticalLayout content = new VerticalLayout(grid);
        content.addClassName("content");
        content.setSizeFull();
        return content;
    }

    private void updateList() {
        grid.setItems(purchaseService.getPurchases(filterText.getValue()));
    }

    private Component getBottomBar() {
        TextField productCode = new TextField();
        productCode.setPlaceholder("Product code");
        TextField deliveryCode = new TextField();
        deliveryCode.setPlaceholder("Delivery code");
        NumberField units = new NumberField();
        units.setPlaceholder("Amount");
        NumberField nettoprice = new NumberField();
        nettoprice.setPlaceholder("Price");
        Button addButton = new Button("Create new");

        addButton.addClickListener(e -> validateAndSave(new PurchaseDTO(productCode.getValue(),
                                                                       deliveryCode.getValue(),
                                                                       units.getValue(),
                                                                       nettoprice.getValue())));

        HorizontalLayout bottomBar = new HorizontalLayout(productCode, deliveryCode, units, nettoprice, addButton);
        bottomBar.addClassName("bottom-bar");
        bottomBar.setHeight("3em");
        addListener(PurchasesView.SaveEvent.class, this::save);
        return bottomBar;
    }

    private void validateAndSave(PurchaseDTO p) {
        try {
            Purchase purchase = p.toPurchase();
            Product product = productService.getProduct(p.productCode);
            Stock stock = product.getStock();
            stock.setUnits(stock.getUnits().add(purchase.getUnits()));
            purchase.setProduct(product);
            Delivery delivery = deliveryService.getDelivery(p.deliveryCode);
            delivery.setNetto(delivery.getNetto().add(purchase.getNettosum()));
            purchase.setDelivery(delivery);
            fireEvent(new SaveEvent(this, purchase, stock, delivery));

        } catch (Exception e) {
            e.printStackTrace();
            ErrorNotification.errorNotification("Cannot create purchase.");
        }
    }

    private void save(PurchasesView.SaveEvent event) {
        purchaseService.save(event.getPurchase());
        stockService.save(event.getStock());
        deliveryService.save(event.getDelivery());
        
        updateList();
    }

    public static abstract class PurchasesEvent extends ComponentEvent<PurchasesView> {
        private Purchase purchase;

        protected PurchasesEvent(PurchasesView source, Purchase purchase) {
            super(source, false);
            this.purchase = purchase;
        }

        public Purchase getPurchase() {
            return purchase;
        }
    }

    public static class SaveEvent extends PurchasesEvent {
        Stock stock;
        Delivery delivery;

        SaveEvent(PurchasesView source, Purchase purchase, Stock stock, Delivery delivery) {
            super(source, purchase);
            this.stock = stock;
            this.delivery = delivery;
        }

        public Stock getStock() {
            return stock;
        }

        public Delivery getDelivery() {
            return delivery;
        }
    }

    public <T extends ComponentEvent<?>> Registration addListener(Class<T> eventType,
                                                                  ComponentEventListener<T> listener) {
        return getEventBus().addListener(eventType, listener);
    }
}