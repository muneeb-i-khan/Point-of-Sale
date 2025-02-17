package com.increff.pos.db.pojo;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "orders")
public class OrderPojo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private double totalAmount;
    private String orderDate;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL)
    private List<SalesPojo> salesItems;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public double getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(double totalAmount) {
        this.totalAmount = totalAmount;
    }

    public String getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(String orderDate) {
        this.orderDate = orderDate;
    }

    public List<SalesPojo> getSalesItems() {
        return salesItems;
    }

    public void setSalesItems(List<SalesPojo> salesItems) {
        this.salesItems = salesItems;
    }
}



