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

import java.io.IOException;
import java.util.List;

@Api
@RestController
@RequestMapping("/api/inventory")
public class InventoryController {

    @Autowired
    private InventoryDto inventoryDto;

    @ApiOperation(value = "Post an inventory")
    @PostMapping
    public ResponseEntity<String> addInventory(@RequestBody InventoryForm inventoryForm) throws ApiException {
        inventoryDto.addInventory(inventoryForm);
        return ResponseEntity.ok("Inventory added successfully.");
    }

    @ApiOperation(value = "Get all inventories")
    @GetMapping
    public ResponseEntity<List<InventoryData>> getAll() throws ApiException {
        List<InventoryData> inventories = inventoryDto.getAllInventories();
        return ResponseEntity.ok(inventories);
    }

    @ApiOperation(value = "Get inventory")
    @GetMapping("/{id}")
    public ResponseEntity<InventoryData> get(@PathVariable Long id) throws ApiException {
        InventoryData inventory = inventoryDto.getInventory(id);
        return ResponseEntity.ok(inventory);
    }

    @ApiOperation(value = "Get inventory based on barcode")
    @GetMapping("/barcode/{barcode}")
    public ResponseEntity<InventoryData> getByBarcode(@PathVariable String barcode) throws ApiException {
        InventoryData inventory = inventoryDto.getInventory(barcode);
        return ResponseEntity.ok(inventory);
    }

    @ApiOperation(value = "Update an existing inventory")
    @PutMapping("/{id}")
    public ResponseEntity<String> updateInventory(@PathVariable Long id, @RequestBody InventoryForm inventoryForm) throws ApiException {
        inventoryDto.updateInventory(inventoryForm, id);
        return ResponseEntity.ok("Inventory updated successfully.");
    }

    @ApiOperation(value = "Upload inventory via TSV")
    @PostMapping("/upload")
    public ResponseEntity<String> uploadInventory(@RequestParam("file") MultipartFile file) throws IOException, ApiException {
        inventoryDto.uploadInventory(file);
        return ResponseEntity.ok("Inventory file uploaded successfully.");
    }
}
