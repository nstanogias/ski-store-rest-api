package com.nstanogias.skistore.specifications;

import lombok.Data;

@Data
public class ProductSpecParams {
    private final int maxPageSize = 50;
    private int pageIndex = 1;
    private int pageSize = 6;
    private int brandId;
    private int typeId;
    private String sort;
    private String search;

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize > maxPageSize ? maxPageSize : pageSize;
    }

}
