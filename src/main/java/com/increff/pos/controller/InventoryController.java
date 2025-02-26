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
import java.util.Collections;
import java.util.List;
import java.util.Map;

@Api
@RestController
@RequestMapping("/api/inventory")
public class InventoryController {

    @Autowired
    private InventoryDto inventoryDto;

    @PostMapping
    public ResponseEntity<Map<String, String>> addInventory(@RequestBody InventoryForm inventoryForm) throws ApiException {
        inventoryDto.addInventory(inventoryForm);
        return ResponseEntity.ok(Collections.singletonMap("message", "Inventory added successfully"));
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

    @PutMapping("/{id}")
    public ResponseEntity<Map<String, String>> updateInventory(@PathVariable Long id, @RequestBody InventoryForm inventoryForm) throws ApiException {
        inventoryDto.updateInventory(inventoryForm, id);
        return ResponseEntity.ok(Collections.singletonMap("message", "Inventory updated successfully"));
    }

    @PostMapping("/upload")
    public ResponseEntity<Map<String, String>> uploadInventory(@RequestParam("file") MultipartFile file) throws IOException, ApiException {
        inventoryDto.uploadInventory(file);
        return ResponseEntity.ok(Collections.singletonMap("message", "Inventory file uploaded successfully"));
    }
}
