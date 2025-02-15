package com.increff.pos.dto;

import com.increff.pos.db.pojo.OrderPojo;
import com.increff.pos.model.OrderData;

public class OrderDto {

    public static OrderData convert(OrderPojo p) {
        OrderData data = new OrderData(p);
        data.setId(p.getId());
        data.setTotalAmount(p.getTotalAmount());
        data.setOrderDate(p.getOrderDate());
        return data;
    }
}
