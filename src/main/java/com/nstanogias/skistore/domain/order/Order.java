package com.nstanogias.skistore.domain.order;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "orders")
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String buyerEmail;
    private Date orderDate = new Date();

    @OneToOne(cascade = {CascadeType.ALL})
    private Address shipToAddress;

    @OneToOne
    private DeliveryMethod deliveryMethod;

    @JsonManagedReference
    @OneToMany(
            mappedBy = "order",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    private List<OrderItem> orderItems = new ArrayList<>();

    private double subTotal;

    @Enumerated(EnumType.STRING)
    private OrderStatus status = OrderStatus.Pending;

    private String paymentIntentId;

    private double getTotal() {
        return subTotal + deliveryMethod.getPrice();
    }

    public void addOrderItem(OrderItem orderItem) {
        orderItems.add(orderItem);
        orderItem.setOrder(this);
    }

    public void removeOrderItem(OrderItem orderItem) {
        orderItems.remove(orderItem);
        orderItem.setOrder(null);
    }
}
