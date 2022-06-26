package com.store.application.content.product;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class ProductDTO {
    public String name;
    public String shortname;
    public String measure;
    public String code;
    public String producer;

    public Product toProduct() {
        Product product = new Product();
        product.name = name;
        product.shortname = shortname;
        product.measure = measure;
        product.code = code;
        return product;
    }
}
