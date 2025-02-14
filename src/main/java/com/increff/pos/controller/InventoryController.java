package com.increff.pos.controller;

import com.increff.pos.db.pojo.ClientPojo;
import com.increff.pos.db.pojo.InventoryPojo;
import com.increff.pos.db.pojo.ProductPojo;
import com.increff.pos.model.ClientData;
import com.increff.pos.model.InventoryData;
import com.increff.pos.model.InventoryForm;
import com.increff.pos.model.ProductForm;
import com.increff.pos.service.ApiException;
import com.increff.pos.service.InventoryService;
import com.increff.pos.service.ProductService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@Api
@RestController
public class InventoryController {
    @Autowired
    ProductService productService;
    @Autowired
    InventoryService inventoryService;

    @ApiOperation(value = "Post an inventory")
    @RequestMapping(path="/api/inventory/", method = RequestMethod.POST)
    public void add(@RequestBody InventoryForm inventoryForm) throws ApiException {
        InventoryPojo inventoryPojo = convert(inventoryForm);
        inventoryService.addInventory(inventoryPojo);
    }

    @ApiOperation(value = "Get all inventories")
    @RequestMapping(path = "/api/inventory", method = RequestMethod.GET)
    public List<InventoryData> getAll() {
        List<InventoryPojo> list = inventoryService.getAllInventories();
        List<InventoryData> list2 = new ArrayList<InventoryData>();
        for(InventoryPojo p : list) {
            list2.add(convert(p));
        }
        return list2;
    }

    @ApiOperation(value = "Get inventory")
    @RequestMapping(path = "/api/inventory/{id}", method = RequestMethod.GET)
    public InventoryData get(@PathVariable  Long id) throws ApiException {
        inventoryService.getCheck(id);
        InventoryPojo inventoryPojo = inventoryService.getInventory(id);
        return convert(inventoryPojo);
    }

    @ApiOperation(value = "Update an existing inventory")
    @RequestMapping(path = "/api/inventory/{id}", method = RequestMethod.PUT)
    public void update(@PathVariable Long id, @RequestBody InventoryForm inventoryForm) throws ApiException{
        InventoryPojo inventoryPojo = convert(inventoryForm);
        inventoryService.getCheck(id);
        inventoryService.updateInventory(id,inventoryPojo);
    }

    @ApiOperation(value = "Delete a inventory")
    @RequestMapping(path="/api/inventory/{id}", method = RequestMethod.DELETE)
    public void delete(@PathVariable Long id) throws ApiException {
        inventoryService.getCheck(id);
        inventoryService.deleteInventory(id);
    }

    public static InventoryData convert(InventoryPojo inventoryPojo) {
        InventoryData inventoryData = new InventoryData();
        inventoryData.setId(inventoryPojo.getId());
        inventoryData.setBarcode(inventoryPojo.getBarcode());
        inventoryData.setQuantity(inventoryPojo.getQuantity());
        inventoryData.setPrice(inventoryPojo.getPrice());
        inventoryData.setProdName(inventoryPojo.getProductPojo().getName());
        inventoryData.setProdId(inventoryPojo.getProductPojo().getId());
        inventoryData.setClientName(inventoryPojo.getProductPojo().getClientPojo().getName());
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
