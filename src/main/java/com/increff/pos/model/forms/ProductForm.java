package com.increff.pos.model.forms;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

@Setter
@Getter
public class ProductForm {
    @NotBlank(message = "Product name can't be blank")
    private String name;
    @NotBlank(message = "Barcode can't be blank")
    private String barcode;
    @Positive(message = "Price has to be positive")
    private double price;
    @NotBlank(message = "Client name can't be blank")
    private String clientName;
}
