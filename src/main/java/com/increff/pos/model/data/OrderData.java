package com.increff.pos.model.data;

import lombok.Getter;
import lombok.Setter;
import java.time.ZonedDateTime;
import java.util.List;

@Getter
@Setter
public class OrderData {
    private Long id;
    private Double totalAmount;
    private ZonedDateTime orderDate;
    private List<OrderItem> items;
    private String customerName;
    private String customerPhone;

    @Getter
    @Setter
    public static class OrderItem {  
        private double price;
        private String barcode;
        private int quantity;
        private String prodName;
        private Double sellingPrice;
    }
}
