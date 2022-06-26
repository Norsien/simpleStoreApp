package com.store.application.views;

import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.applayout.DrawerToggle;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.HighlightActions;
import com.vaadin.flow.router.HighlightConditions;
import com.vaadin.flow.router.RouterLink;

@CssImport(value = "./styles/styles.css")
public class MainLayout extends AppLayout {
    public MainLayout() {
        createHeader();
        createDrawer();
    }

    private void createDrawer() {
        H1 logo = new H1("Store info");
        logo.addClassNames("text-l", "m-m");

        HorizontalLayout header = new HorizontalLayout(new DrawerToggle(), logo);

        header.setDefaultVerticalComponentAlignment(FlexComponent.Alignment.CENTER);
        header.expand(logo);
        header.setWidthFull();
        header.addClassNames("py-0", "px-m");

        addToNavbar(header);
    }

    private void createHeader() {
        RouterLink homeView = new RouterLink("Home", MainView.class);
        homeView.setHighlightCondition(HighlightConditions.sameLocation());
        homeView.setHighlightAction(HighlightActions.toggleTheme("blue"));

        RouterLink deliveryView = new RouterLink("Delivery", DeliveryView.class);
        deliveryView.setHighlightCondition(HighlightConditions.sameLocation());
        deliveryView.setHighlightAction(HighlightActions.toggleTheme("blue"));

        RouterLink productView = new RouterLink("Products", ProductsView.class);
        productView.setHighlightCondition(HighlightConditions.sameLocation());
        productView.setHighlightAction(HighlightActions.toggleTheme("blue"));

        RouterLink producerView = new RouterLink("Producers", ProducersView.class);
        producerView.setHighlightCondition(HighlightConditions.sameLocation());
        producerView.setHighlightAction(HighlightActions.toggleTheme("blue"));

        RouterLink purchasesView = new RouterLink("Purchases", PurchasesView.class);
        purchasesView.setHighlightCondition(HighlightConditions.sameLocation());
        purchasesView.setHighlightAction(HighlightActions.toggleTheme("blue"));

        RouterLink receiptsView = new RouterLink("Receipts", ReceiptsView.class);
        receiptsView.setHighlightCondition(HighlightConditions.sameLocation());
        receiptsView.setHighlightAction(HighlightActions.toggleTheme("blue"));

        RouterLink salesView = new RouterLink("Sales", SalesView.class);
        salesView.setHighlightCondition(HighlightConditions.sameLocation());
        salesView.setHighlightAction(HighlightActions.toggleTheme("blue"));

        RouterLink stockView = new RouterLink("Stock", StockView.class);
        stockView.setHighlightCondition(HighlightConditions.sameLocation());
        stockView.setHighlightAction(HighlightActions.toggleTheme("blue"));

        addToDrawer(new VerticalLayout(
            homeView,
            stockView,
            productView,
            producerView,
            deliveryView,
            purchasesView,
            receiptsView,
            salesView
        ));


    }
}
