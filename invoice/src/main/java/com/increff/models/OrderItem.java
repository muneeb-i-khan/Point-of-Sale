package com.increff.models;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OrderItem {
    private String barcode;
    private String prodName;
    private Double price;
    private Integer quantity;
    private Double sellingPrice;
}

