package com.nstanogias.skistore.domain.order;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.*;

import javax.persistence.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class OrderItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(cascade = {CascadeType.ALL})
    private ProductItemOrdered itemOrdered;

    @JsonBackReference
    @ManyToOne(fetch = FetchType.LAZY)
    private Order order;

    private double price;
    private int quantity;
}
