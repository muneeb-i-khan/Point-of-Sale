package com.increff.pos.model.forms;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.*;

@Getter
@Setter
public class InventoryForm {
    @NotBlank(message = "Barcode can't be blank")
    @Size(max = 255, message = "Barcode can be maximum of size 255")
    private String barcode;
    @PositiveOrZero(message = "Quantity can't be negative")
    private Long quantity;
}
