package com.store.application.views;

import com.store.application.content.producer.ProducerService;
import com.store.application.content.product.Product;
import com.store.application.content.product.ProductDTO;
import com.store.application.content.product.ProductService;
import com.store.application.content.stock.Stock;
import com.store.application.content.stock.StockService;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.ComponentEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Anchor;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.shared.Registration;

@PageTitle("Store | Products")
@Route(value = "products", layout = MainLayout.class)
public class ProductsView extends VerticalLayout {

    TextField filterText = new TextField();
    Grid<Product> grid = new Grid<>();
    private ProductService productService;
    private ProducerService producerService;
    private StockService stockService;

    public ProductsView(ProductService productService, 
                        ProducerService producerService, 
                        StockService stockService) {
        this.productService = productService;
        this.producerService = producerService;
        this.stockService = stockService;
        addClassName("product-table-view");
        setSizeFull();

        configureGrid();
        
        add(
            getContent(),
            getBottomBar()
        );

        updateList();
    }
    
    private void configureGrid() {
        grid.addClassName("product-grid");
        grid.setSizeFull();
        grid.addColumn(p -> p.getName()).setHeader("Name").setSortable(true);
        grid.addColumn(p -> p.getShortname()).setHeader("Short name").setSortable(true);
        grid.addColumn(p -> p.getMeasure()).setHeader("Measure").setSortable(true);
        grid.addColumn(p -> p.getCode()).setHeader("Code").setSortable(true);
        grid.addColumn(p -> p.getProducer().getName()).setHeader("Producer").setSortable(true);
        grid.addComponentColumn(p -> new Anchor("product/" + p.getCode(), "go"))
            .setHeader("Navigate");
        grid.getColumns().forEach(col -> col.setAutoWidth(true));
    }

    private Component getContent() {
        VerticalLayout content = new VerticalLayout(grid);
        content.addClassName("content");
        content.setSizeFull();
        return content;
    }

    private void updateList() {
        grid.setItems(productService.getProducts(filterText.getValue()));
    }

    private Component getBottomBar() {
        TextField name = new TextField();
        name.setPlaceholder("Name");
        TextField shortName = new TextField();
        shortName.setPlaceholder("Short name");
        TextField measure = new TextField();
        measure.setPlaceholder("Measure");
        TextField code = new TextField();
        code.setPlaceholder("Code");
        TextField producer = new TextField();
        producer.setPlaceholder("Producer");
        Button addButton = new Button("Create new");

        addButton.addClickListener(e -> validateAndSave(new ProductDTO(name.getValue(),
                                                                       shortName.getValue(),
                                                                       measure.getValue(),
                                                                       code.getValue(),
                                                                       producer.getValue())));

        HorizontalLayout bottomBar = new HorizontalLayout(name, shortName, measure, code, producer, addButton);
        bottomBar.addClassName("bottom-bar");
        bottomBar.setHeight("3em");
        addListener(ProductsView.SaveEvent.class, this::createnew);
        return bottomBar;
    }

    private void validateAndSave(ProductDTO p) {
        try {
            Product product = p.toProduct();
            product.setProducer(producerService.getProducer(p.producer));
            Stock stock = new Stock(product);
            product.setStock(stock);
            fireEvent(new SaveEvent(this, product, stock));

        } catch (Exception e) {
            e.printStackTrace();
            ErrorNotification.errorNotification("Cannot create product.");
        }
    }

    private void createnew(ProductsView.SaveEvent event) {
        productService.save(event.getProduct());
        stockService.save(event.getStock());
        updateList();
    }

    public static abstract class ProductsEvent extends ComponentEvent<ProductsView> {
        private Product product;

        protected ProductsEvent(ProductsView source, Product product) {
            super(source, false);
            this.product = product;
        }

        public Product getProduct() {
            return product;
        }
    }

    public static class SaveEvent extends ProductsEvent {
        private Stock stock;
        
        SaveEvent(ProductsView source, Product product, Stock stock) {
            super(source, product);
            this.stock = stock;
        }

        public Stock getStock() {
            return stock;
        }
    }

    public <T extends ComponentEvent<?>> Registration addListener(Class<T> eventType,
                                                                  ComponentEventListener<T> listener) {
        return getEventBus().addListener(eventType, listener);
    }
}