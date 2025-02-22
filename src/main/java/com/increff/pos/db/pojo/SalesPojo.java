package com.increff.pos.db.pojo;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@Table(name = "sales")
public class SalesPojo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "product_id")
    private ProductPojo product;

    private int quantity;
    private double unitPrice;
    private double totalAmount;

    @ManyToOne
    @JoinColumn(name = "order_id")
    private OrderPojo order;

    private String saleDate;

    private String prodName;
}
