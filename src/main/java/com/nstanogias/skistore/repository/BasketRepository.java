package com.nstanogias.skistore.repository;

import com.nstanogias.skistore.domain.CustomerBasket;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface BasketRepository extends JpaRepository<CustomerBasket, Long> {
    Optional<CustomerBasket> findByCid(String cid);
    void deleteByCid(String cid);
}
