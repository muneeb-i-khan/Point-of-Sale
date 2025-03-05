package com.increff.pos.model.data;

import lombok.Getter;
import lombok.Setter;

import java.time.ZonedDateTime;
import java.util.List;

@Getter
@Setter
public class OrderData {
    private Long                id;
    private Double              totalAmount;
    private ZonedDateTime           orderDate;
    private List<OrderItem>     items;
    private String              customerName;
    private String              customerPhone;
}
