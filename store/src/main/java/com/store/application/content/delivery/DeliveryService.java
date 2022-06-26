package com.store.application.content.delivery;

import java.util.List;

import org.springframework.stereotype.Service;

import com.store.application.exceptions.ItemNotFoundException;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class DeliveryService {
    private final DeliveryRepository repository;

    public List<Delivery> getDeliveries(String filterText) {
        List<Delivery> deliveries;
        if (filterText == null || filterText == "") {
        deliveries = repository.findAll();
        } else {
            deliveries = repository.findAll();;
        }
        return deliveries;
    }

    public void save(Delivery d) {
        repository.save(d);
    }

    public Delivery getDelivery(String deliveryCode) {
        Delivery delivery = repository.findByCode(deliveryCode)
                .orElseThrow(() -> new ItemNotFoundException("Delivery", "code", deliveryCode));
        return delivery;
    }

    public Delivery get(String deliveryCode) {
        Delivery delivery = repository.findByCode(deliveryCode)
            .orElse(null);
        return delivery;
    }
}
