package com.store.application.views;

import com.store.application.content.stock.Stock;
import com.store.application.content.stock.StockService;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

@PageTitle("Store | Stock")
@Route(value = "stocks", layout = MainLayout.class)
public class StockView extends VerticalLayout {
    
    TextField filterText = new TextField();
    Grid<Stock> grid = new Grid<>();
    private StockService stockService;

    public StockView(StockService stockService) {
        this.stockService = stockService;
        addClassName("stock-table-view");
        setSizeFull();

        configureGrid();
        
        add(
            getContent()
        );

        updateList();
    }
    
    private void configureGrid() {
        grid.addClassName("stock-grid");
        grid.setSizeFull();
        grid.addColumn(p -> p.getProduct().getCode()).setHeader("Stock code").setSortable(true);
        grid.addColumn(p -> p.getUnits()).setHeader("Units").setSortable(true);
        grid.getColumns().forEach(col -> col.setAutoWidth(true));
    }

    private Component getContent() {
        VerticalLayout content = new VerticalLayout(grid);
        content.addClassName("content");
        content.setSizeFull();
        return content;
    }

    private void updateList() {
        grid.setItems(stockService.get(filterText.getValue()));
    }
}
