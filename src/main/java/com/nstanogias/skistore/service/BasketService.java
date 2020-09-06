package com.nstanogias.skistore.service;

import com.nstanogias.skistore.domain.BasketItem;
import com.nstanogias.skistore.domain.CustomerBasket;
import com.nstanogias.skistore.dtos.CustomerBasketDto;
import com.nstanogias.skistore.repository.BasketRepository;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Optional;

@Slf4j
@Service
public class BasketService {
    @Autowired
    ModelMapper modelMapper;

    @Autowired
    CacheManager cacheManager;

    @Autowired
    BasketRepository basketRepository;

    @Caching(put = {
            @CachePut(value = "findByCidCache", key = "#customerBasketDto.cid")
    })
    public CustomerBasketDto insert(CustomerBasketDto customerBasketDto) {
        Optional<CustomerBasket> basket = basketRepository.findByCid(customerBasketDto.getCid());
        if (basket.isPresent()) {
            CustomerBasket basketToUpdate = basket.get();
            basketToUpdate.setClientSecret(customerBasketDto.getClientSecret());
            basketToUpdate.setPaymentIntentId(customerBasketDto.getPaymentIntentId());
            basketToUpdate.setShippingPrice(customerBasketDto.getShippingPrice());
            basketToUpdate.removeAll();
            customerBasketDto.getItems().forEach(basketItemDto -> {
                basketToUpdate.addItem(modelMapper.map(basketItemDto, BasketItem.class));
            });
            basketRepository.save(basketToUpdate);
            log.info("basket updated");
        } else {
            CustomerBasket basketToCreate = new CustomerBasket();
            basketToCreate.setCid(customerBasketDto.getCid());
            basketToCreate.setClientSecret(customerBasketDto.getClientSecret());
            basketToCreate.setPaymentIntentId(customerBasketDto.getPaymentIntentId());
            basketToCreate.setShippingPrice(customerBasketDto.getShippingPrice());
            customerBasketDto.getItems().forEach(basketItemDto -> {
                basketToCreate.addItem(modelMapper.map(basketItemDto, BasketItem.class));
            });
            basketRepository.save(basketToCreate);
            log.info("basket created");
        }
        return customerBasketDto;
    }

    @Cacheable(
            cacheNames = "findByCidCache",
            key = "#cid",
            unless = "#result == null"
    )
    public CustomerBasketDto findByCid(String cid) {
        Optional<CustomerBasket> findCustomerBasket = basketRepository.findByCid(cid);
        if(findCustomerBasket.isPresent()) {
            return modelMapper.map(findCustomerBasket.get(), CustomerBasketDto.class);
        } else {
            log.error("CustomerBasket not found");
            return null;
        }
    }

    @Transactional
    @CacheEvict(cacheNames = "findByCidCache", key = "#cid")
    public void deleteByCid(String cid) {
        basketRepository.deleteByCid(cid);
    }
}
