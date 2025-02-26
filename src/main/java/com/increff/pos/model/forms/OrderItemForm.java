package com.increff.pos.model.forms;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.util.List;

@Getter
@Setter
public class OrderItemForm {
  @Positive
  private Long      quantity;
  @NotNull
  @NotBlank
  private String    barcode;
}
