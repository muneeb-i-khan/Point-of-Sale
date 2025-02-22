package com.increff.pos.model.data;

import com.increff.pos.db.pojo.OrderPojo;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
public class OrderData {
    private Long id;
    private Double totalAmount;
    private String orderDate;
    private List<SaleItem> items;

    public OrderData(OrderPojo order) {
        this.id = order.getId();
        this.totalAmount = order.getTotalAmount();
        this.orderDate = order.getOrderDate();
        this.items = order.getSalesItems()
                .stream()
                .map(SaleItem::new)
                .collect(Collectors.toList());
    }
}
