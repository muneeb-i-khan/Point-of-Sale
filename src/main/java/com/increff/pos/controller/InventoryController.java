package com.increff.pos.controller;

import com.increff.pos.dto.InventoryDto;
import com.increff.pos.model.InventoryData;
import com.increff.pos.model.InventoryForm;
import com.increff.pos.service.ApiException;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Api
@RestController
public class InventoryController {
    @Autowired
    InventoryDto inventoryDto;

    @ApiOperation(value = "Post an inventory")
    @RequestMapping(path="/api/inventory/", method = RequestMethod.POST)
    public void addInventory(@RequestBody InventoryForm inventoryForm) throws ApiException {
        inventoryDto.addInventory(inventoryForm);
    }

    @ApiOperation(value = "Get all inventories")
    @RequestMapping(path = "/api/inventory", method = RequestMethod.GET)
    public List<InventoryData> getAll() {

    }

    @ApiOperation(value = "Get inventory")
    @RequestMapping(path = "/api/inventory/{id}", method = RequestMethod.GET)
    public InventoryData get(@PathVariable  Long id) throws ApiException {
        return inventoryDto.getInventory(id);
    }

    @ApiOperation(value = "Get inventory based on barcode")
    @RequestMapping(path = "/api/inventory/{barcode}", method = RequestMethod.GET)
    public InventoryData get(@PathVariable  String barcode) throws ApiException {
       return  inventoryDto.getInventory(barcode);
    }

    @ApiOperation(value = "Update an existing inventory")
    @RequestMapping(path = "/api/inventory/{id}", method = RequestMethod.PUT)
    public void updateInventory(@PathVariable Long id, @RequestBody InventoryForm inventoryForm) throws ApiException{
        inventoryDto.updateInventory(inventoryForm, id);
    }

    @ApiOperation(value = "Delete a inventory")
    @RequestMapping(path="/api/inventory/{id}", method = RequestMethod.DELETE)
    public void deleteInventory(@PathVariable Long id) throws ApiException {
        inventoryDto.deleteInventory(id);
    }

}
