package com.nstanogias.skistore.dtos;

import lombok.Data;

@Data
public class ProductToReturn {
    private int id;
    private String name;
    private String description;
    private double price;
    private String pictureUrl;
    private String productType;
    private String productBrand;
}
