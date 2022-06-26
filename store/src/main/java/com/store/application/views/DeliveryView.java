package com.store.application.views;

import com.store.application.content.delivery.Delivery;
import com.store.application.content.delivery.DeliveryDTO;
import com.store.application.content.delivery.DeliveryService;
import java.sql.Timestamp;
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

@PageTitle("Store | Deliverys")
@Route(value = "deliverys", layout = MainLayout.class)
public class DeliveryView extends VerticalLayout {

    TextField filterText = new TextField();
    Grid<Delivery> grid = new Grid<>();
    private DeliveryService deliveryService;

    public DeliveryView(DeliveryService deliveryService) {
        this.deliveryService = deliveryService;
        addClassName("delivery-table-view");
        setSizeFull();

        configureGrid();
        
        add(
            getContent(),
            getBottomBar()
        );

        updateList();
    }
    
    private void configureGrid() {
        grid.addClassName("delivery-grid");
        grid.setSizeFull();
        grid.addColumn(p -> p.getCode()).setHeader("Code").setSortable(true);
        grid.addColumn(p -> p.getTime()).setHeader("Time").setSortable(true);
        grid.addColumn(p -> p.getNetto()).setHeader("Netto").setSortable(true);
        grid.getColumns().forEach(col -> col.setAutoWidth(true));
    }

    private Component getContent() {
        VerticalLayout content = new VerticalLayout(grid);
        content.addClassName("content");
        content.setSizeFull();
        return content;
    }

    private void updateList() {
        grid.setItems(deliveryService.getDeliveries(filterText.getValue()));
    }

    private Component getBottomBar() {
        TextField code = new TextField();
        code.setPlaceholder("Code");
        DateTimePicker  time = new DateTimePicker();
        //time.setPlaceholder("Time");
        Button addButton = new Button("Create new");
        addButton.addClickListener(e -> validateAndSave(new DeliveryDTO(code.getValue(),
                                                                       Timestamp.valueOf(time.getValue()))));

        HorizontalLayout bottomBar = new HorizontalLayout(code, time, addButton);
        bottomBar.addClassName("bottom-bar");
        bottomBar.setHeight("3em");
        addListener(DeliveryView.SaveEvent.class, this::save);
        return bottomBar;
    }

    private void validateAndSave(DeliveryDTO p) {
        try {
            Delivery delivery = p.toDelivery();
            fireEvent(new SaveEvent(this, delivery));
        } catch (Exception e) {
            e.printStackTrace();
            ErrorNotification.errorNotification("Cannot create delivery.");
        }
    }

    private void save(DeliveryView.SaveEvent event) {
        deliveryService.save(event.getDelivery());
        updateList();
    }

    public static abstract class DeliverysEvent extends ComponentEvent<DeliveryView> {
        private Delivery delivery;

        protected DeliverysEvent(DeliveryView source, Delivery delivery) {
            super(source, false);
            this.delivery = delivery;
        }

        public Delivery getDelivery() {
            return delivery;
        }
    }

    public static class SaveEvent extends DeliverysEvent {
        SaveEvent(DeliveryView source, Delivery delivery) {
            super(source, delivery);
        }
    }

    public <T extends ComponentEvent<?>> Registration addListener(Class<T> eventType,
                                                                  ComponentEventListener<T> listener) {
        return getEventBus().addListener(eventType, listener);
    }
}