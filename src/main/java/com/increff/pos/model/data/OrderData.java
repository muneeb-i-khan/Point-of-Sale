package com.increff.pos.model.data;

import com.increff.pos.db.pojo.OrderPojo;

import java.util.List;
import java.util.stream.Collectors;

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

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Double getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(Double totalAmount) {
        this.totalAmount = totalAmount;
    }

    public String getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(String orderDate) {
        this.orderDate = orderDate;
    }

    public List<SaleItem> getItems() {
        return items;
    }

    public void setItems(List<SaleItem> items) {
        this.items = items;
    }
}
