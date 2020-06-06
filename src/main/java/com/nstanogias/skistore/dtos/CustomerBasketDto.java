package com.nstanogias.skistore.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CustomerBasketDto implements Serializable {

    @NotNull
    private String cid;

    private List<BasketItemDto> items;
}
