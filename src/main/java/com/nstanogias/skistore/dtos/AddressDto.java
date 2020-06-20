package com.nstanogias.skistore.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AddressDto {
    private String firstName;
    private String lastName;
    private String street;
    private String city;
    private String state;
    private String zipcode;
}
