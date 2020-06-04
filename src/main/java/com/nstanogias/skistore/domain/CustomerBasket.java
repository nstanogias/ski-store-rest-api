package com.nstanogias.skistore.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@Entity
public class CustomerBasket implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String cid;

    @OneToMany(
            mappedBy = "customerBasket",
            cascade = CascadeType.MERGE,
            orphanRemoval = true
    )
    private List<BasketItem> items;

    public void addItem(BasketItem item) {
        items.add(item);
        item.setCustomerBasket(this);
    }

    public void removeItem(BasketItem item ) {
        items.remove(item);
        item.setCustomerBasket(null);
    }
}
