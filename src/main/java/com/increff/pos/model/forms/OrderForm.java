package com.increff.pos.model.forms;

import lombok.Getter;
import lombok.Setter;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;
import java.util.List;

@Getter
@Setter
public class OrderForm {
    @Valid
    private CustomerForm customer;
    @Valid
    private List<OrderItemForm> orderItems;

    @Getter
    @Setter
    public static class OrderItemForm {
        @Positive(message = "Quantity has to be positive")
        private Long quantity;
        @NotBlank(message = "Barcode can't be blank")
        @Size(max = 255, message = "Barcode can be maximum of size 255")
        private String barcode;
        @Positive(message = "Selling price has to be positive")
        private Double sellingPrice;
    }

}
