package com.store.application.content.sale;
import java.math.BigDecimal;

import javax.persistence.Column;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import com.store.application.content.product.Product;
import com.store.application.content.receipt.Receipt;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@Entity
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Table(name = "Sale")
public class Sale {
    @Id
    @SequenceGenerator(name = "content_sequence", sequenceName = "content_sequence", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "content_sequence")
    @Column(updatable = false)
    @EqualsAndHashCode.Include
    private Long id;

    @ManyToOne
    @JoinColumn(name = "product_id")
    private Product product;

    @ManyToOne
    @JoinColumn(name = "receipt_id")
    private Receipt receipt;

    @Column(nullable = false)
    private BigDecimal units;

    @Column(nullable = false)
    private BigDecimal brutto;

    @Column(nullable = false)
    private BigDecimal netto;

    @Column(nullable = false)
    private BigDecimal vatpercentage;

    @Column(nullable = false)
    private BigDecimal bruttosum;

    @Column(nullable = false)
    private BigDecimal nettosum;

    @Column(nullable = false)
    private BigDecimal vatsum;
}
