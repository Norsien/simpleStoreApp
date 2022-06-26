package com.store.application.views;

import java.sql.Timestamp;

import com.store.application.content.receipt.Receipt;
import com.store.application.content.receipt.ReceiptDTO;
import com.store.application.content.receipt.ReceiptService;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.ComponentEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.datetimepicker.DateTimePicker;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.shared.Registration;

@PageTitle("Store | Receipts")
@Route(value = "receipts", layout = MainLayout.class)
public class ReceiptsView extends VerticalLayout {
    
    TextField filterText = new TextField();
    Grid<Receipt> grid = new Grid<>();
    private ReceiptService receiptService;

    public ReceiptsView(ReceiptService receiptService) {
        this.receiptService = receiptService;
        addClassName("receipt-table-view");
        setSizeFull();

        configureGrid();
        
        add(
            getContent(),
            getBottomBar()
        );

        updateList();
    }
    
    private void configureGrid() {
        grid.addClassName("receipt-grid");
        grid.setSizeFull();
        grid.addColumn(p -> p.getCode()).setHeader("Code").setSortable(true);
        grid.addColumn(p -> p.getTime()).setHeader("Time").setSortable(true);
        grid.addColumn(p -> p.getNetto()).setHeader("Netto").setSortable(true);
        grid.addColumn(p -> p.getBrutto()).setHeader("Brutto").setSortable(true);
        grid.addColumn(p -> p.getVat()).setHeader("VAT").setSortable(true);
        grid.getColumns().forEach(col -> col.setAutoWidth(true));
    }

    private Component getContent() {
        VerticalLayout content = new VerticalLayout(grid);
        content.addClassName("content");
        content.setSizeFull();
        return content;
    }

    private void updateList() {
        grid.setItems(receiptService.getReceipts(""));
    }

    private Component getBottomBar() {
        DateTimePicker time = new DateTimePicker();
        //time.setPlaceholder("Time");
        TextField recipitCode = new TextField();
        recipitCode.setPlaceholder("Recipt code");
        Button addButton = new Button("Create new");
        addButton.addClickListener(e -> validateAndSave(new ReceiptDTO(recipitCode.getValue(),
                                                        Timestamp.valueOf(time.getValue()))));

        HorizontalLayout bottomBar = new HorizontalLayout(recipitCode, time, addButton);
        bottomBar.addClassName("bottom-bar");
        bottomBar.setHeight("3em");
        addListener(ReceiptsView.SaveEvent.class, this::save);
        return bottomBar;
    }

    private void validateAndSave(ReceiptDTO p) {
        try {
            Receipt receipt = p.toReceipt();
            fireEvent(new SaveEvent(this, receipt));
        } catch (Exception e) {
            e.printStackTrace();
            ErrorNotification.errorNotification("Cannot create receipt.");
        }
    }

    private void save(ReceiptsView.SaveEvent event) {
        receiptService.save(event.getReceipt());
        updateList();
    }

    public static abstract class ReceiptsEvent extends ComponentEvent<ReceiptsView> {
        private Receipt receipt;

        protected ReceiptsEvent(ReceiptsView source, Receipt receipt) {
            super(source, false);
            this.receipt = receipt;
        }

        public Receipt getReceipt() {
            return receipt;
        }
    }

    public static class SaveEvent extends ReceiptsEvent {
        SaveEvent(ReceiptsView source, Receipt receipt) {
            super(source, receipt);
        }
    }

    public <T extends ComponentEvent<?>> Registration addListener(Class<T> eventType,
                                                                  ComponentEventListener<T> listener) {
        return getEventBus().addListener(eventType, listener);
    }


}
