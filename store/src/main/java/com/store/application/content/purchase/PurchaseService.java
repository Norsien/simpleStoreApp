package com.store.application.content.purchase;

import java.util.List;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PurchaseService {
    private final PurchaseRepository repository;

    public List<Purchase> getPurchases(String filterText) {
        List<Purchase> purchases;
        if (filterText == null || filterText == "") {
            purchases = repository.findAll();
        } else {
            purchases = repository.findByProductCode(filterText);
        }
        return purchases;
    }
    
    public void save(Purchase p) {
        repository.save(p);
    }

}
