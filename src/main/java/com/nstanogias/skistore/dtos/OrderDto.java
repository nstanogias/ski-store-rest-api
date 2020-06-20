package com.nstanogias.skistore.dtos;

import lombok.Data;

@Data
public class OrderDto {
    private String basketId;
    private long deliveryMethodId;
    private AddressDto shipToAddress;
}
