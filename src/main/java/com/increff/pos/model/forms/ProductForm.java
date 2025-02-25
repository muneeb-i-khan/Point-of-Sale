package com.increff.pos.model.forms;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class ProductForm {
    private String      name;
    private String      barcode;
    private double      price;
    private String      clientName;
}
