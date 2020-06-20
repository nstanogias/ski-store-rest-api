package com.nstanogias.skistore.service;

import com.nstanogias.skistore.domain.CustomerBasket;
import com.nstanogias.skistore.domain.Product;
import com.nstanogias.skistore.domain.order.*;
import com.nstanogias.skistore.dtos.AddressDto;
import com.nstanogias.skistore.repository.BasketRepository;
import com.nstanogias.skistore.repository.DeliveryMethodRepository;
import com.nstanogias.skistore.repository.OrderRepository;
import com.nstanogias.skistore.repository.ProductRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@AllArgsConstructor
public class OrdersService {

    @Autowired
    ModelMapper modelMapper;

    @NotNull
    private final OrderRepository orderRepository;

    @NotNull
    private final DeliveryMethodRepository deliveryMethodRepository;
    @NotNull
    private final ProductRepository productRepository;

    @NotNull
    private final BasketRepository basketRepository;

    @Transactional
    public Order createOrder(String buyerEmail, long deliveryMethodId, String basketId, AddressDto shippingAddress) {

        Optional<CustomerBasket> basket = basketRepository.findByCid(basketId);

        if(basket.isPresent()) {
            DeliveryMethod deliveryMethod = deliveryMethodRepository.getOne(deliveryMethodId);
            Order orderToCreate = new Order();
            basket.get().getItems().forEach(item -> {
                Product product = productRepository.findById(item.getId()).get();
                ProductItemOrdered productItemOrdered = new ProductItemOrdered();
                productItemOrdered.setProductItemId(product.getId());
                productItemOrdered.setProductName(product.getName());
                productItemOrdered.setPictureUrl(product.getPictureUrl());
                OrderItem orderItem = new OrderItem();
                orderItem.setItemOrdered(productItemOrdered);
                orderItem.setPrice(product.getPrice());
                orderItem.setQuantity(item.getQuantity());
                orderToCreate.addOrderItem(orderItem);
            });
            double subtotal = orderToCreate.getOrderItems().stream().mapToDouble(orderItem -> orderItem.getPrice() * orderItem.getQuantity()).reduce(0.0, Double::sum);
            orderToCreate.setBuyerEmail(buyerEmail);
            orderToCreate.setShipToAddress(modelMapper.map(shippingAddress, Address.class));
            orderToCreate.setDeliveryMethod(deliveryMethod);
            orderToCreate.setSubTotal(subtotal);
            orderRepository.save(orderToCreate);
            basketRepository.deleteByCid(basketId);

            return orderToCreate;
        } else {
            log.error("basket not found");
        }
        return null;
    }

    public List<DeliveryMethod> getDeliveryMethods() {
        return deliveryMethodRepository.findAll();
    }

    public Order getOrderById(long id, String buyerEmail) {
        return orderRepository.findByIdAndAndBuyerEmail(id, buyerEmail);
    }

    public List<Order> getOrdersForUser(String buyerEmail) {
        return orderRepository.findByBuyerEmail(buyerEmail);
    }
}
