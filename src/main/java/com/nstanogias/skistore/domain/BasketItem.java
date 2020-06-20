package com.nstanogias.skistore.domain;

import lombok.*;

import javax.persistence.*;
import java.io.Serializable;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class BasketItem implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String productName;
    private double price;
    private int quantity;
    private String pictureUrl;
    private String brand;
    private String type;

    @ManyToOne(fetch = FetchType.LAZY)
    private CustomerBasket customerBasket;
}
