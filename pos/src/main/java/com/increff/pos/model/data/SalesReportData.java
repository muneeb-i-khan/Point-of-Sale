package com.increff.pos.model.data;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SalesReportData {
    private String clientName;
    private String description;
    private Long quantity;
    private Long revenue;
}
