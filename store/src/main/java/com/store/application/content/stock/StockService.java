package com.store.application.content.stock;

import java.util.List;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class StockService {
    private final StockRepository repository;

    public List<Stock> get(String filterText) {
        List<Stock> stocks;
        if (filterText == null || filterText == "") {
            stocks = repository.findAll();
        } else {
            stocks = repository.findAll();
        }
        return stocks;
    }
    
    public void save(Stock p) {
        repository.save(p);
    }
}
