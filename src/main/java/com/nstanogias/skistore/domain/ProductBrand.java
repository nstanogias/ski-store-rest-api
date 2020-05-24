package com.nstanogias.skistore.domain;

public enum ProductBrand {
    Angular("Angular"), Net("Net Core"), Vscode("VS Code"), React("React"), Typescript("Typescript"), Redis("Redis");

    private final String name;

    ProductBrand(String name) {
        this.name = name;
    }
}
