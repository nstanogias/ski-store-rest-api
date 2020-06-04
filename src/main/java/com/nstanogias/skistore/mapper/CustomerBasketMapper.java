package com.nstanogias.skistore.mapper;

import com.nstanogias.skistore.domain.CustomerBasket;
import com.nstanogias.skistore.dtos.CustomerBasketDto;
import org.mapstruct.Mapper;

@Mapper
public interface CustomerBasketMapper {
    CustomerBasketDto customerBasketToCustomerBasketDto(CustomerBasket customerBasket);
    CustomerBasket customerBasketDtoToCustomerBasket(CustomerBasketDto customerBasketDto);
}
