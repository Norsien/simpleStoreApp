package com.store.application.content.product;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import com.store.application.content.producer.Producer;
import com.store.application.content.stock.Stock;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@Entity
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Table(name = "product")
public class Product {
    @Id
    @SequenceGenerator(name = "content_sequence", sequenceName = "content_sequence", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "content_sequence")
    @Column(updatable = false)
    @EqualsAndHashCode.Include
    private Long id;
    
    @Column(nullable = false, length = 65)
    String name;

    @Column(nullable = false, length = 17)
    String shortname;

    @Column(nullable = false, length = 3)
    String measure;

    @EqualsAndHashCode.Include
    @Column(nullable = false, length = 13, unique = true)
    String code;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "stock_id")
    Stock stock;

    @ManyToOne
    @JoinColumn(name = "producer_id")
    Producer producer;
}
