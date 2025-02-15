package com.increff.pos.controller;

import com.increff.pos.db.pojo.InventoryPojo;
import com.increff.pos.dto.InventoryDto;
import com.increff.pos.model.InventoryData;
import com.increff.pos.model.InventoryForm;
import com.increff.pos.service.ApiException;
import com.increff.pos.service.InventoryService;
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
    InventoryService inventoryService;

    @ApiOperation(value = "Post an inventory")
    @RequestMapping(path="/api/inventory/", method = RequestMethod.POST)
    public void add(@RequestBody InventoryForm inventoryForm) throws ApiException {
        InventoryPojo inventoryPojo = InventoryDto.convert(inventoryForm);
        inventoryService.addInventory(inventoryPojo);
    }

    @ApiOperation(value = "Get all inventories")
    @RequestMapping(path = "/api/inventory", method = RequestMethod.GET)
    public List<InventoryData> getAll() {
        List<InventoryPojo> list = inventoryService.getAllInventories();
        List<InventoryData> list2 = new ArrayList<InventoryData>();
        for(InventoryPojo p : list) {
            list2.add(InventoryDto.convert(p));
        }
        return list2;
    }

    @ApiOperation(value = "Get inventory")
    @RequestMapping(path = "/api/inventory/{id}", method = RequestMethod.GET)
    public InventoryData get(@PathVariable  Long id) throws ApiException {
        inventoryService.getCheck(id);
        InventoryPojo inventoryPojo = inventoryService.getInventory(id);
        return InventoryDto.convert(inventoryPojo);
    }

    @ApiOperation(value = "Update an existing inventory")
    @RequestMapping(path = "/api/inventory/{id}", method = RequestMethod.PUT)
    public void update(@PathVariable Long id, @RequestBody InventoryForm inventoryForm) throws ApiException{
        InventoryPojo inventoryPojo = InventoryDto.convert(inventoryForm);
        inventoryService.getCheck(id);
        inventoryService.updateInventory(id,inventoryPojo);
    }

    @ApiOperation(value = "Delete a inventory")
    @RequestMapping(path="/api/inventory/{id}", method = RequestMethod.DELETE)
    public void delete(@PathVariable Long id) throws ApiException {
        inventoryService.getCheck(id);
        inventoryService.deleteInventory(id);
    }

}
