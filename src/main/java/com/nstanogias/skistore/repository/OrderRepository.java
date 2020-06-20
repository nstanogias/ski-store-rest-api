package com.nstanogias.skistore.repository;

import com.nstanogias.skistore.domain.order.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
    Order findByIdAndAndBuyerEmail(Long id, String buyerEmail);

    List<Order> findByBuyerEmail(String buyerEmail);
}
