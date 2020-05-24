package com.nstanogias.skistore.domain;

public enum ProductType {
    Boards("Boards"), Hats("Hats"), Boots("Boots"), Gloves("Gloves");

    private final String name;

    ProductType(String name) {
        this.name = name;
    }
}
