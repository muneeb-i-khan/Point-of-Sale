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

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
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
    public void addInventory(@RequestBody InventoryForm inventoryForm) throws ApiException {
        inventoryDto.addInventory(inventoryForm);
    }

    @ApiOperation(value = "Get all inventories")
    @GetMapping
    public List<InventoryData> getAll() throws ApiException {
        return inventoryDto.getAllInventories();
    }

    @ApiOperation(value = "Get inventory")
    @GetMapping("/{id}")
    public InventoryData get(@PathVariable Long id) throws ApiException {
        return inventoryDto.getInventory(id);
    }

    @CrossOrigin(origins = "http://localhost:4200", exposedHeaders = "totalInventories")
    @ApiOperation(value = "Get all inventories with pagination")
    @GetMapping("/paginated")
    public List<InventoryData> getAllPaginated(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "15") int pageSize, HttpServletResponse httpServletResponse) throws ApiException {
        return inventoryDto.getAllInventoriesPaginated(page, pageSize, httpServletResponse);
    }

    @ApiOperation(value = "Get inventory based on barcode")
    @GetMapping("/barcode/{barcode}")
    public InventoryData getByBarcode(@PathVariable String barcode) throws ApiException {
        return inventoryDto.getInventory(barcode);
    }

    @PutMapping("/{id}")
    public void updateInventory(@PathVariable Long id, @RequestBody InventoryForm inventoryForm) throws ApiException {
        inventoryDto.updateInventory(inventoryForm, id);
    }

    @PostMapping("/upload")
    public void uploadInventory(@RequestParam("file") MultipartFile file) throws IOException, ApiException {
        inventoryDto.uploadInventory(file);
    }
}
