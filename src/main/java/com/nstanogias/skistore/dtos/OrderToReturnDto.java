package com.nstanogias.skistore.dtos;

import com.nstanogias.skistore.domain.order.Address;
import com.nstanogias.skistore.domain.order.OrderItem;
import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
public class OrderToReturnDto {
    private int id;
    private String buyerEmail;
    private Date orderDate;
    private Address shipToAddress;
    private String deliveryMethod;
    private double shippingPrice;
    private List<OrderItem> orderItems;
    private double subTotal;
    private double total;
    private String status;
}
