package com.increff.pos.model.data;

import com.increff.pos.db.pojo.OrderPojo;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class OrderData {
    private Long                id;
    private Double              totalAmount;
    private String              orderDate;
    private List<OrderItem>     items;
}
