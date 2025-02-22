package com.increff.pos.model.data;

import com.increff.pos.db.pojo.SalesPojo;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class SaleItem {
    private String barcode;
    private int quantity;
    private String saleDate;
    private String prodName;


    public SaleItem(SalesPojo salesPojo) {
        this.barcode = salesPojo.getProduct().getBarcode();
        this.quantity = salesPojo.getQuantity();
        this.saleDate = salesPojo.getSaleDate();
        this.prodName = salesPojo.getProdName();
    }
}
