package com.increff.pos.model.data;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
public class OrderData {
    private Long                id;
    private Double              totalAmount;
    private LocalDate           orderDate;
    private List<OrderItem>     items;
    private String              customerName;
    private String              customerPhone;
}
