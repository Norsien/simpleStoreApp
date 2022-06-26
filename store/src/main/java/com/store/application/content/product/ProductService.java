package com.store.application.content.product;

import java.util.List;

import org.springframework.stereotype.Service;

import com.store.application.exceptions.ItemNotFoundException;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ProductService {
    private final ProductRepository repository;

    public List<Product> getProducts(String filterText) {
        List<Product> products;
        if (filterText == null || filterText == "") {
            products = repository.findAll();
        } else {
            products = repository.findByNameContaining(filterText);
        }
        return products;
    }

    public Product getProduct(String code) throws Exception {
        Product product = repository.findByCode(code)
            .orElseThrow(() -> new ItemNotFoundException("Product", "code", code));
        return product;
    }

    public Product get(String code) {
        Product product = repository.findByCode(code)
            .orElse(null);
        return product;
    }
    
    public void save(Product p) {
        repository.save(p);
    }
}
