package com.nstanogias.skistore.helper;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class Pagination<T> {
    int pageIndex;
    int pageSize;
    long count;
    List<T> data;
}
