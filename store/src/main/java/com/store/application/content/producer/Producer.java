package com.store.application.content.producer;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import com.store.application.content.product.Product;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Entity
@Data
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@Table(name = "producer")
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Producer {
    @Id
    @SequenceGenerator(name = "content_sequence", sequenceName = "content_sequence", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "content_sequence")
    @Column(updatable = false)
    @EqualsAndHashCode.Include
    protected Long id;
    
    @Column(nullable = false, length = 129)
    String name;

    @EqualsAndHashCode.Include
    @Column(nullable = false, length = 10, unique = true)
    String code;

    @Column(length = 129)
    String email;

    @OneToMany(mappedBy = "producer")
    private List<Product> products;

    
}
