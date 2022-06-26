package com.store.application.content.sale;

import java.math.BigDecimal;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class SaleDTO {
    public String receiptCode;
    public String productCode;
    public double units;
    public double netto;
    public double brutto;
    public double vatpercentage;
    
    public Sale toSale(){
        Sale sale = new Sale();
        sale.setUnits(new BigDecimal(units));
        sale.setNetto(new BigDecimal(netto));
        sale.setBrutto(new BigDecimal(brutto));
        sale.setVatpercentage(new BigDecimal(vatpercentage));
        sale.setBruttosum(new BigDecimal(brutto*units));
        sale.setNettosum(new BigDecimal(netto*units));
        sale.setVatsum(new BigDecimal((brutto-netto)*units));

        return sale;
    }
}
