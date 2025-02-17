package com.increff.pos.dto;

import com.increff.pos.db.pojo.InventoryPojo;
import com.increff.pos.db.pojo.ProductPojo;
import com.increff.pos.model.InventoryData;
import com.increff.pos.model.InventoryForm;
import com.increff.pos.service.ApiException;
import com.increff.pos.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;

public class InventoryDto {
    @Autowired
    static ProductService productService;


}
