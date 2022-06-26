package com.store.application.content.producer;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

public interface ProducerRepository extends JpaRepository<Producer, Long> {
    List<Producer> findByNameContaining(String infix);
    Optional<Producer> findByCode(String code);
}
