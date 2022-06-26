package com.store.application.content.purchase;

import java.math.BigDecimal;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class PurchaseDTO {
    public String productCode;
    public String deliveryCode;
    public double units;
    public double nettoprice;

    public Purchase toPurchase() {
        Purchase purchase = new Purchase();
        purchase.setUnits(new BigDecimal(units));
        purchase.setNettoprice(new BigDecimal(nettoprice));
        purchase.setNettosum(new BigDecimal(units * nettoprice));
        return purchase;
    }
}
