package com.store.application.content.receipt;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

public interface ReceiptRepository extends JpaRepository<Receipt, Long> {

    Optional<Receipt> findByCode(String receiptCode);
    
}
