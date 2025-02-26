package com.increff.pos.model.forms;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

@Getter
@Setter
public class InventoryForm {
    @NotBlank
    @NotNull
    private String      barcode;
    @Positive
    private Long        quantity;
}
