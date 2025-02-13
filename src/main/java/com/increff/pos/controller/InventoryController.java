package com.increff.pos.controller;

import com.increff.pos.db.pojo.InventoryPojo;
import com.increff.pos.db.pojo.ProductPojo;
import com.increff.pos.model.InventoryData;
import com.increff.pos.model.InventoryForm;
import com.increff.pos.service.ApiException;
import com.increff.pos.service.InventoryService;
import com.increff.pos.service.ProductService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@Api
@RestController
public class InventoryController {
    @Autowired
    ProductService productService;
    @Autowired
    InventoryService inventoryService;

    @ApiOperation(value = "Post an inventory")
    @RequestMapping(path="/api/inventory/", method = RequestMethod.POST)
    public void addInventory(@RequestBody InventoryForm inventoryForm) throws ApiException {
        InventoryPojo inventoryPojo = convert(inventoryForm);
        inventoryService.addInventory(inventoryPojo);
    }

    public static InventoryData convert(InventoryPojo inventoryPojo) {
        InventoryData inventoryData = new InventoryData();
        inventoryData.setId(inventoryPojo.getId());
        inventoryData.setBarcode(inventoryPojo.getBarcode());
        inventoryData.setQuantity(inventoryPojo.getQuantity());
        inventoryData.setPrice(inventoryPojo.getPrice());
        inventoryData.setProdName(inventoryPojo.getProductPojo().getName());
        inventoryData.setProdId(inventoryPojo.getProductPojo().getId());
        return inventoryData;
    }

    public InventoryPojo convert(InventoryForm inventoryForm) throws ApiException {
        InventoryPojo inventoryPojo = new InventoryPojo();
        inventoryPojo.setBarcode(inventoryForm.getBarcode());
        inventoryPojo.setQuantity(inventoryForm.getQuantity());
        inventoryPojo.setPrice(inventoryForm.getPrice());
        ProductPojo productPojo = productService.getProductByBarcode(inventoryForm.getBarcode());
        inventoryPojo.setProductPojo(productPojo);
        return inventoryPojo;
    }

}
