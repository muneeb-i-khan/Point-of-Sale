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
    public static InventoryData convert(InventoryPojo inventoryPojo) {
        InventoryData inventoryData = new InventoryData();
        inventoryData.setId(inventoryPojo.getId());
        inventoryData.setBarcode(inventoryPojo.getBarcode());
        inventoryData.setQuantity(inventoryPojo.getQuantity());
        inventoryData.setProdName(inventoryPojo.getProductPojo().getName());
        inventoryData.setProdId(inventoryPojo.getProductPojo().getId());
        inventoryData.setClientName(inventoryPojo.getProductPojo().getClientPojo().getName());
        return inventoryData;
    }

    public static InventoryPojo convert(InventoryForm inventoryForm) throws ApiException {
        InventoryPojo inventoryPojo = new InventoryPojo();
        inventoryPojo.setBarcode(inventoryForm.getBarcode());
        inventoryPojo.setQuantity(inventoryForm.getQuantity());
        ProductPojo productPojo = productService.getProductByBarcode(inventoryForm.getBarcode());
        inventoryPojo.setProductPojo(productPojo);
        return inventoryPojo;
    }

}
