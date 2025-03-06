package com.increff.pos.db.pojo;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter
@Setter
@Table(name = "orderItem")
public class OrderItemPojo extends AbstractPojo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long prodId;
    private Long quantity;
    private Long orderId;
    private Double sellingPrice;
}
