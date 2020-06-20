package com.nstanogias.skistore.domain;

import com.nstanogias.skistore.domain.order.Address;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String displayName;
    private String email;
    private String password;

    @OneToOne(cascade = {CascadeType.ALL})
    private Address address;
}
