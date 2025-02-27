package com.increff.pos.model.forms;

import lombok.Getter;
import lombok.Setter;

import javax.validation.Valid;
import java.util.List;

@Getter
@Setter
public class OrderForm {
    @Valid
    private CustomerForm customer;

    @Valid
    private List<OrderItemForm> orderItems;
}
