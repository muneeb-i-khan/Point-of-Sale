package com.increff.pos.dto;

import com.increff.pos.db.pojo.InventoryPojo;
import com.increff.pos.db.pojo.ProductPojo;
import com.increff.pos.model.InventoryData;
import com.increff.pos.model.InventoryForm;
import com.increff.pos.service.ApiException;
import com.increff.pos.service.InventoryService;
import com.increff.pos.service.ProductService;
import com.increff.pos.service.TsvUploadService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Component
public class InventoryDto {
    @Autowired
    ProductService productService;

    @Autowired
    InventoryService inventoryService;

    @Autowired
    TsvUploadService tsvUploadService;

    public void addInventory(InventoryForm inventoryForm) throws ApiException {
        InventoryPojo inventoryPojo = convert(inventoryForm);
        inventoryService.addInventory(inventoryPojo);
    }

    public List<InventoryData> getAllInventories() {
        List<InventoryPojo> list = inventoryService.getAllInventories();
        List<InventoryData> list2 = new ArrayList<InventoryData>();
        for(InventoryPojo p : list) {
            list2.add(convert(p));
        }
        return list2;
    }

    public InventoryData getInventory(Long id) throws ApiException{
        inventoryService.getCheck(id);
        InventoryPojo inventoryPojo = inventoryService.getInventory(id);
        return convert(inventoryPojo);
    }

    public InventoryData getInventory(String barcode) throws ApiException{
        ProductPojo productPojo = productService.getProductByBarcode(barcode);
        inventoryService.getCheck(productPojo.getId());
        InventoryPojo inventoryPojo = inventoryService.getInventory(productPojo.getId());
        return convert(inventoryPojo);
    }

    public void updateInventory(InventoryForm inventoryForm, Long id) throws ApiException {
        InventoryPojo inventoryPojo = convert(inventoryForm);
        inventoryService.getCheck(id);
        inventoryService.updateInventory(id,inventoryPojo);
    }

    public void deleteInventory(Long id) throws ApiException {
        inventoryService.getCheck(id);
        inventoryService.deleteInventory(id);
    }

    public void uploadInventory(MultipartFile file) throws IOException, ApiException {
        tsvUploadService.uploadInventory(file);
    }

    public InventoryData convert(InventoryPojo inventoryPojo) {
        InventoryData inventoryData = new InventoryData();
        inventoryData.setId(inventoryPojo.getId());
        inventoryData.setBarcode(inventoryPojo.getBarcode());
        inventoryData.setQuantity(inventoryPojo.getQuantity());
        inventoryData.setProdName(inventoryPojo.getProductPojo().getName());
        inventoryData.setProdId(inventoryPojo.getProductPojo().getId());
        inventoryData.setClientName(inventoryPojo.getProductPojo().getClientPojo().getName());
        return inventoryData;
    }

    public InventoryPojo convert(InventoryForm inventoryForm) throws ApiException {
        InventoryPojo inventoryPojo = new InventoryPojo();
        inventoryPojo.setBarcode(inventoryForm.getBarcode());
        inventoryPojo.setQuantity(inventoryForm.getQuantity());
        ProductPojo productPojo = productService.getProductByBarcode(inventoryForm.getBarcode());
        inventoryPojo.setProductPojo(productPojo);
        return inventoryPojo;
    }

}
