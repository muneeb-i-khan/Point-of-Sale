package com.increff.pos.controller;

import com.increff.pos.dto.InventoryDto;
import com.increff.pos.model.data.InventoryData;
import com.increff.pos.model.forms.InventoryForm;
import com.increff.pos.service.ApiException;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Api
@RestController
@RequestMapping("/api/inventory")
public class InventoryController {
    @Autowired
    InventoryDto inventoryDto;

    @ApiOperation(value = "Post an inventory")
    @PostMapping
    public void addInventory(@RequestBody InventoryForm inventoryForm) throws ApiException {
        inventoryDto.addInventory(inventoryForm);
    }

    @ApiOperation(value = "Get all inventories")
    @GetMapping
    public List<InventoryData> getAll() {
        return inventoryDto.getAllInventories();
    }

    @ApiOperation(value = "Get inventory")
    @GetMapping("/{id}")
    public InventoryData get(@PathVariable Long id) throws ApiException {
        return inventoryDto.getInventory(id);
    }

    @ApiOperation(value = "Get inventory based on barcode")
    @GetMapping("/barcode/{barcode}")
    public InventoryData getByBarcode(@PathVariable String barcode) throws ApiException {
        return inventoryDto.getInventory(barcode);
    }

    @ApiOperation(value = "Update an existing inventory")
    @PutMapping("/{id}")
    public void updateInventory(@PathVariable Long id, @RequestBody InventoryForm inventoryForm) throws ApiException {
        inventoryDto.updateInventory(inventoryForm, id);
    }

//    @ApiOperation(value = "Delete an inventory")
//    @DeleteMapping("/{id}")
//    public void deleteInventory(@PathVariable Long id) throws ApiException {
//        inventoryDto.deleteInventory(id);
//    }

    @ApiOperation(value = "Upload inventory via TSV")
    @PostMapping("/upload")
    public ResponseEntity<String> uploadInventory(@RequestParam("file") MultipartFile file) throws Exception {
        inventoryDto.uploadInventory(file);
        return ResponseEntity.ok("Inventory file uploaded successfully.");
    }
}
