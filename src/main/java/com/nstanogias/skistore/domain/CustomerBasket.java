package com.nstanogias.skistore.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
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
    private List<BasketItem> items = new ArrayList<>();

    public void addItem(BasketItem item) {
        items.add(item);
        item.setCustomerBasket(this);
    }

    public void removeItem(BasketItem item) {
        items.remove(item);
        item.setCustomerBasket(null);
    }
}
