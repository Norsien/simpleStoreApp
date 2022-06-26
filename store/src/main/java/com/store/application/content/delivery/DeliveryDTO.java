package com.store.application.content.delivery;

import java.math.BigDecimal;
import java.sql.Timestamp;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class DeliveryDTO {
    public String code;
    public Timestamp time;

    public Delivery toDelivery() {
        Delivery delivery = new Delivery();
        delivery.setCode(code);
        delivery.setTime(time);
        delivery.setNetto(new BigDecimal(0));
        return delivery;
    }
}
