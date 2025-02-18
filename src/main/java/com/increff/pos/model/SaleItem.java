package com.increff.pos.model;

import com.increff.pos.db.pojo.SalesPojo;

public class SaleItem {
    private String barcode;
    private int quantity;
    private String saleDate;
    private String prodName;

    public String getProdName() {
        return prodName;
    }

    public void setProdName(String prodName) {
        this.prodName = prodName;
    }

    public SaleItem(SalesPojo salesPojo) {
        this.barcode = salesPojo.getProduct().getBarcode();
        this.quantity = salesPojo.getQuantity();
        this.saleDate = salesPojo.getSaleDate();
        this.prodName = salesPojo.getProdName();
    }

    public String getBarcode() {
        return barcode;
    }

    public void setBarcode(String barcode) {
        this.barcode = barcode;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public String getSaleDate() {
        return saleDate;
    }

    public void setSaleDate(String saleDate) {
        this.saleDate = saleDate;
    }
}
