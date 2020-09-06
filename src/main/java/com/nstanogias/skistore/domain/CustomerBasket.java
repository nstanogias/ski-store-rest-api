package com.nstanogias.skistore.domain;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import lombok.*;

import javax.persistence.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@JsonIdentityInfo(
        generator = ObjectIdGenerators.PropertyGenerator.class,
        property = "id")
public class CustomerBasket implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String cid;

    @OneToMany(
            mappedBy = "customerBasket",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    private Set<BasketItem> items = new HashSet<>();

    private Long deliveryMethodId;
    private String clientSecret;
    private String paymentIntentId;
    private double shippingPrice;

    public void addItem(BasketItem item) {
        items.add(item);
        item.setCustomerBasket(this);
    }

    public void removeItem(BasketItem item) {
        items.remove(item);
        item.setCustomerBasket(null);
    }

    public void addAll(Set<BasketItem> items) {
        items.addAll(items);
        items.forEach(item -> item.setCustomerBasket(this));
    }

    public void removeAll() {
        items.clear();
        items.forEach(item -> item.setCustomerBasket(null));
    }
}
