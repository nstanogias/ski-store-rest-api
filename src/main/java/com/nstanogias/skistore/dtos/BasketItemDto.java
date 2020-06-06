package com.nstanogias.skistore.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BasketItemDto implements Serializable {

    @NotNull
    private String productName;

    @NotNull
    @DecimalMin(value = "0.1", message = "Price must be greater than zero")
    private double price;

    @NotNull
    @Min(value = 1, message = "Quantity must be at least 1")
    private int quantity;

    @NotNull
    private String pictureUrl;

    @NotNull
    private String brand;

    @NotNull
    private String type;
}