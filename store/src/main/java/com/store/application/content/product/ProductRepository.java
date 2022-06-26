package com.store.application.content.product;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, Long> {
    List<Product> findByNameContaining(String infix);

    Optional<Product> findByCode(String code);
}
