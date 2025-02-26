package com.increff.pos.model.forms;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

@Setter
@Getter
public class ProductForm {
    @NotNull
    @NotBlank
    private String      name;
    @NotNull
    @NotBlank
    private String      barcode;
    @Positive
    private double      price;
    @NotNull
    @NotBlank
    private String      clientName;
}
