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
  @Positive(message = "Quantity has to be positive")
  private Long quantity;
  @NotBlank(message = "Barcode can't be blank")
  private String barcode;
  @Positive(message = "Selling price has to be positive")
  private Double sellingPrice;
}
