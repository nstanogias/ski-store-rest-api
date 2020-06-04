package com.nstanogias.skistore.dtos;

import com.nstanogias.skistore.domain.BasketItem;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CustomerBasketDto implements Serializable {
    private String cid;
    private List<BasketItem> items;
}
