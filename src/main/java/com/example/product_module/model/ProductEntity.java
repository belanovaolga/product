package com.example.product_module.model;

import jakarta.persistence.Id;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Column;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "product")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ProductEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "productSeqGen")
    @SequenceGenerator(name = "productSeqGen", sequenceName = "product_sequence_name", allocationSize = 1)
    private Long id;
    private String name;
    private String description;
    private Long count;
    @Column(name = "current_price")
    private Double currentPrice;
}
