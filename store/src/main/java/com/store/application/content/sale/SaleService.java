package com.store.application.content.sale;

import java.util.List;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class SaleService {
    private final SaleRepository repository;

    public List<Sale> getSales(String filterText) {
        List<Sale> sales;
        if (filterText == null || filterText == "") {
            sales = repository.findAll();
        } else {
            sales = repository.findByProductCode(filterText);
        }
        return sales;
    }
    
    public void save(Sale p) {
        repository.save(p);
    }

}