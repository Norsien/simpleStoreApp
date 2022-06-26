package com.store.application.content.receipt;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import com.store.application.content.sale.Sale;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@Entity
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Table(name = "receipt")
public class Receipt {
    @Id
    @SequenceGenerator(name = "content_sequence", sequenceName = "content_sequence", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "content_sequence")
    @Column(updatable = false)
    @EqualsAndHashCode.Include
    protected Long id;

    @Column(nullable = false)
    @EqualsAndHashCode.Include
    private Timestamp time;

    @Column(nullable = false)
    @EqualsAndHashCode.Include
    private String code;

    @Column(nullable = false)
    private BigDecimal netto;

    @Column(nullable = false)
    private BigDecimal brutto;

    @Column(nullable = false)
    private BigDecimal vat;

    @OneToMany(mappedBy = "receipt")
    private List<Sale> productsSold;
}
