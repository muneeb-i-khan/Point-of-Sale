package com.increff.pos.model.data;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class OrderItem {
    private double      price;
    private String      barcode;
    private int         quantity;
    private String      prodName;
    private Double      sellingPrice;
}
