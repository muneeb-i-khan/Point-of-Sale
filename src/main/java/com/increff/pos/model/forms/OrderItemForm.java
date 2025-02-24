package com.increff.pos.model.forms;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class OrderItemForm {
  private Long      quantity;
  private String    barcode;
}
