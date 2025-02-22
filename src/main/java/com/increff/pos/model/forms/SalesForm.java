package com.increff.pos.model.forms;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class SalesForm {
    private List<SaleItem> items;

    @Getter
    @Setter
    public static class SaleItem {
        private String barcode;
        private int quantity;
        private String saleDate;
    }
}
