package com.store.application.content.receipt;

import java.math.BigDecimal;
import java.sql.Timestamp;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class ReceiptDTO {
    public String code;
    public Timestamp time;

    public Receipt toReceipt() {
        Receipt receipt = new Receipt();
        receipt.setCode(code);
        receipt.setTime(time);
        receipt.setNetto(new BigDecimal(0.0));
        receipt.setBrutto(new BigDecimal(0.0));
        receipt.setVat(new BigDecimal(0.0));
        return receipt;
    }
}
