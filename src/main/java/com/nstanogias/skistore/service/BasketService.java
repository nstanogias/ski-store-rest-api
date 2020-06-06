package com.nstanogias.skistore.service;

import com.nstanogias.skistore.domain.CustomerBasket;
import com.nstanogias.skistore.dtos.CustomerBasketDto;
import com.nstanogias.skistore.mapper.CustomerBasketMapper;
import com.nstanogias.skistore.repository.BasketRepository;
import lombok.extern.slf4j.Slf4j;
import org.mapstruct.factory.Mappers;
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
    CacheManager cacheManager;

    @Autowired
    BasketRepository basketRepository;

    private CustomerBasketMapper customerBasketMapper = Mappers.getMapper(CustomerBasketMapper.class);

    @Caching(put = {
            @CachePut(value = "findByCidCache", key = "#customerBasketDto.cid")
    })
    public CustomerBasketDto insert(CustomerBasketDto customerBasketDto) {
        log.info("Update: Updating cache with name: findAllCache and findByCidCache");
        return customerBasketMapper.customerBasketToCustomerBasketDto(
                basketRepository.save(customerBasketMapper.customerBasketDtoToCustomerBasket(customerBasketDto)));
    }

    @Cacheable(
            cacheNames = "findByCidCache",
            key = "#cid",
            unless = "#result == null"
    )
    public CustomerBasketDto findByCid(String cid) {
        Optional<CustomerBasket> findCustomerBasket = basketRepository.findByCid(cid);
        if(findCustomerBasket.isPresent()) {
            return customerBasketMapper.customerBasketToCustomerBasketDto(findCustomerBasket.get());
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
