package com.store.application.content.delivery;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import com.store.application.content.purchase.Purchase;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@Entity
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Table(name = "delivery")
public class Delivery {
    @Id
    @SequenceGenerator(name = "content_sequence", sequenceName = "content_sequence", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "content_sequence")
    @Column(updatable = false)
    @EqualsAndHashCode.Include
    protected Long id;

    @EqualsAndHashCode.Include
    @Column(nullable = false, length = 17) String code;

    @Column(nullable = false)
    private Date time;

    @Column(nullable = false)
    private BigDecimal netto;

    @OneToMany(mappedBy = "delivery")
    private List<Purchase> productsBought;
}
