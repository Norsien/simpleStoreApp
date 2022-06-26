package com.store.application.content.receipt;

import java.util.List;

import org.springframework.stereotype.Service;

import com.store.application.exceptions.ItemNotFoundException;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ReceiptService {
    private final ReceiptRepository repository;

    public List<Receipt> getReceipts(String filterText) {
        List<Receipt> receipts;
        if (filterText == null || filterText == "") {
            receipts = repository.findAll();
        } else {
            receipts = repository.findAll();
        }
        return receipts;
    }
    
    public void save(Receipt r) {
        repository.save(r);
    }

    public Receipt getReceipt(String receiptCode) {
        Receipt receipt = repository.findByCode(receiptCode)
            .orElseThrow(() -> new ItemNotFoundException("Receipt", "code", receiptCode));
        return receipt;
    }

    public Receipt get(String receiptCode) {
        Receipt receipt = repository.findByCode(receiptCode)
            .orElse(null);
        return receipt;
    }
}
