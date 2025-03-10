package com.increff.pos.model.forms;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;

@Getter
@Setter
public class InventoryForm {
    @NotBlank(message = "Barcode can't be blank")
    @Size(max = 255, message = "Barcode can be maximum of size 255")
    private String barcode;
    @Positive(message = "Quantity has to be positive")
    private Long quantity;
}
