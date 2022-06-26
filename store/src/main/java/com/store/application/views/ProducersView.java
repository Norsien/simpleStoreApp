package com.store.application.views;

import com.store.application.content.producer.Producer;
import com.store.application.content.producer.ProducerDTO;
import com.store.application.content.producer.ProducerService;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.ComponentEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.shared.Registration;

@PageTitle("Store | Producers")
@Route(value = "producers", layout = MainLayout.class)
public class ProducersView extends VerticalLayout {
    
    TextField filterText = new TextField();
    Grid<Producer> grid = new Grid<>();
    private ProducerService producerService;

    public ProducersView(ProducerService producerService) {
        this.producerService = producerService;
        addClassName("producer-table-view");
        setSizeFull();

        configureGrid();
        
        add(
            getContent(),
            getBottomBar()
        );

        updateList();
    }
    
    private void configureGrid() {
        grid.addClassName("producer-grid");
        grid.setSizeFull();
        grid.addColumn(p -> p.getName()).setHeader("Name").setSortable(true);
        grid.addColumn(p -> p.getCode()).setHeader("Code").setSortable(true);
        grid.addColumn(p -> p.getEmail()).setHeader("Email").setSortable(true);
        grid.getColumns().forEach(col -> col.setAutoWidth(true));
    }

    private Component getContent() {
        VerticalLayout content = new VerticalLayout(grid);
        content.addClassName("content");
        content.setSizeFull();
        return content;
    }

    private void updateList() {
        grid.setItems(producerService.getProducers(filterText.getValue()));
    }

    private Component getBottomBar() {
        TextField name = new TextField();
        name.setPlaceholder("Name");
        TextField code = new TextField();
        code.setPlaceholder("Code");
        TextField email = new TextField();
        email.setPlaceholder("Email");
        Button addButton = new Button("Create new");

        addButton.addClickListener(e -> validateAndSave(new ProducerDTO(name.getValue(),
                                                                        code.getValue(),
                                                                        email.getValue())));

        HorizontalLayout bottomBar = new HorizontalLayout(name, code, email, addButton);
        bottomBar.addClassName("bottom-bar");
        bottomBar.setHeight("3em");
        addListener(ProducersView.SaveEvent.class, this::save);
        return bottomBar;
    }

    private void validateAndSave(ProducerDTO p) {
        try {
            Producer producer = p.toProducer();
            fireEvent(new SaveEvent(this, producer));
        } catch (Exception e) {
            e.printStackTrace();
            ErrorNotification.errorNotification("Cannot create producer.");
        }
    }

    private void save(ProducersView.SaveEvent event) {
        producerService.save(event.getProducer());
        updateList();
    }

    public static abstract class ProducersEvent extends ComponentEvent<ProducersView> {
        private Producer producer;

        protected ProducersEvent(ProducersView source, Producer producer) {
            super(source, false);
            this.producer = producer;
        }

        public Producer getProducer() {
            return producer;
        }
    }

    public static class SaveEvent extends ProducersEvent {
        SaveEvent(ProducersView source, Producer producer) {
            super(source, producer);
        }
    }

    public <T extends ComponentEvent<?>> Registration addListener(Class<T> eventType,
                                                                  ComponentEventListener<T> listener) {
        return getEventBus().addListener(eventType, listener);
    }
}
