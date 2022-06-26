package com.store.application.views;

import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.store.application.content.givegain.GiveGain;
import com.store.application.content.product.Product;
import com.store.application.content.product.ProductService;
import com.store.application.content.purchase.Purchase;
import com.store.application.content.purchase.PurchaseService;
import com.store.application.content.sale.Sale;
import com.store.application.content.sale.SaleService;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

@PageTitle("Store | Product Info")
@Route(value = "product/:name", layout = MainLayout.class)
public class ProductInfoView extends VerticalLayout implements BeforeEnterObserver {
    
    @Autowired
    ProductService productService;
    @Autowired
    SaleService saleService;
    @Autowired
    PurchaseService purchaseService;


    private String productCode;
    Grid<Product> productGrid = new Grid<>();
    Grid<GiveGain> grid = new Grid<>();

    Label label = new Label();
    Product product;

    private void configureProductGrid() {
        productGrid.addClassName("product-grid");
        productGrid.setHeight("80");
        productGrid.addColumn(p -> p.getName()).setHeader("Name");
        productGrid.addColumn(p -> p.getShortname()).setHeader("Short name");
        productGrid.addColumn(p -> p.getMeasure()).setHeader("Measure");
        productGrid.addColumn(p -> p.getCode()).setHeader("Code");
        productGrid.addColumn(p -> p.getProducer().getName()).setHeader("Producer Name");
        productGrid.addColumn(p -> p.getProducer().getCode()).setHeader("Producer Code");
        productGrid.getColumns().forEach(col -> col.setAutoWidth(true));
    }

    private void configureGrid() {
        grid.addClassName("grid");
        grid.setHeight("400px");
        grid.addColumn(p -> p.getType()).setHeader("Type");
        grid.addColumn(p -> p.getCode()).setHeader("Code");
        grid.addColumn(p -> p.getUnits()).setHeader("Units");
        grid.addColumn(p -> p.getNetto()).setHeader("Netto");
        grid.addColumn(p -> p.getBrutto()).setHeader("Brutto");
        grid.addColumn(p -> p.getNettoSum()).setHeader("Netto Sum");
        grid.addColumn(p -> p.getBruttoSum()).setHeader("Brutto Sum");
        grid.addColumn(p -> p.getVatPercent()).setHeader("VAT %");
        grid.addColumn(p -> p.getVatSum()).setHeader("VAT Sum");
        grid.getColumns().forEach(col -> col.setAutoWidth(true));
    }

    private Component getContent() {
        TextArea resultField = new TextArea();
        resultField.setWidth("100%");
        Button exportButton = new Button(
            "Export as CSV",
            e -> {
           //     this.export(grid, resultField);
            }
        );
        VerticalLayout content = new VerticalLayout(productGrid, grid);
        content.addClassName("content");
        content.setSizeFull();
        return content;
    }

    @Override
    public void beforeEnter(BeforeEnterEvent event) {
        productCode = event.getRouteParameters().get("name").get();
        Product product = productService.get(productCode);
        if (product == null) {
            add(label);
            label.setText("Product with code: " + productCode + " does not exists.");
        } else {
            setSizeFull();

            configureProductGrid();
            configureGrid();

            add(
                getContent()
            );

            productGrid.setItems(product);
            List<GiveGain> changes = new ArrayList<GiveGain>();
            List<Sale> sales = saleService.getSales(productCode);
            List<Purchase> purchases = purchaseService.getPurchases(productCode);
            for (Sale s : sales) {
                changes.add(new GiveGain(s));
            }
            for (Purchase p : purchases) {
                changes.add(new GiveGain(p));
            }
            grid.setItems(changes);
        }
    }

    // private void export(List<GiveGain> changes, TextArea result) {

    //     StringWriter output = new StringWriter();
    //     StatefulBeanToCsv<Person> writer = new StatefulBeanToCsvBuilder<Person>(output).build();
    //     try {
    //         writer.write(persons);
    //     } catch (CsvDataTypeMismatchException | CsvRequiredFieldEmptyException e) {
    //         output.write("An error occured during writing: " + e.getMessage());
    //     }

    //     result.setValue(output.toString());
    // }

    
}
