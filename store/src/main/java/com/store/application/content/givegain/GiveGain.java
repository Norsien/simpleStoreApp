package com.store.application.content.givegain;

import java.math.BigDecimal;

import com.store.application.content.purchase.Purchase;
import com.store.application.content.sale.Sale;

import lombok.Getter;

@Getter
public class GiveGain {
    public String type;
    public String code;
    public BigDecimal units;
    public BigDecimal netto;
    public BigDecimal nettoSum;
    public BigDecimal brutto;
    public BigDecimal bruttoSum;
    public BigDecimal vatPercent;
    public BigDecimal vatSum;

    public GiveGain(Sale sale){
        this.type = "sold";
        this.code = sale.getReceipt().getCode();
        this.units = sale.getUnits();
        this.netto = sale.getNetto();
        this.nettoSum = sale.getNettosum();
        this.brutto = sale.getBrutto();
        this.bruttoSum = sale.getBruttosum();
        this.vatPercent = sale.getVatpercentage();
        this.vatSum = sale.getVatsum();
    }

    public GiveGain(Purchase purchase){
        this.type = "bought";
        this.code = purchase.getDelivery().getCode();
        this.units = purchase.getUnits();
        this.netto = purchase.getNettoprice().multiply(new BigDecimal(-1));;
        this.nettoSum = purchase.getNettosum().multiply(new BigDecimal(-1));
        this.brutto = new BigDecimal(0);
        this.bruttoSum = new BigDecimal(0);
        this.vatPercent = new BigDecimal(0);
        this.vatSum = new BigDecimal(0);
    }
}
