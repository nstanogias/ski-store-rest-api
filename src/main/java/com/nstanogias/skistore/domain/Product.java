package com.nstanogias.skistore.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String description;
    private double price;
    private String pictureUrl;

    @Enumerated(EnumType.STRING)
    private ProductBrand productBrand;

    @Enumerated(EnumType.STRING)
    private ProductType productType;
}
