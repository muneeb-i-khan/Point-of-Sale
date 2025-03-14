package com.increff.pos.model.data;

import com.increff.pos.model.forms.InventoryForm;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class InventoryData extends InventoryForm {
    private Long        id;
    private Long        prodId;
    private String      prodName;
    private String      clientName;
}
