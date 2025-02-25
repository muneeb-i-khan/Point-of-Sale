package com.increff.pos.dto;

import com.increff.pos.db.pojo.InventoryPojo;
import com.increff.pos.model.data.InventoryData;
import com.increff.pos.model.forms.InventoryForm;
import com.increff.pos.service.ApiException;
import com.increff.pos.service.InventoryService;
import com.increff.pos.flow.InventoryFlow;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Component
public class InventoryDto {
    @Autowired
    private InventoryService inventoryService;

    @Autowired
    private InventoryFlow inventoryFlow;

    public void addInventory(InventoryForm inventoryForm) throws ApiException {
        InventoryPojo inventoryPojo = inventoryFlow.convert(inventoryForm);
        inventoryService.addInventory(inventoryPojo);
    }

    public List<InventoryData> getAllInventories() throws ApiException {
        List<InventoryPojo> list = inventoryService.getAllInventories();
        List<InventoryData> list2 = new ArrayList<>();
        for(InventoryPojo p : list) {
            list2.add(inventoryFlow.convert(p));
        }
        return list2;
    }

    public InventoryData getInventory(Long id) throws ApiException {
        inventoryService.getCheck(id);
        InventoryPojo inventoryPojo = inventoryService.getInventory(id);
        return inventoryFlow.convert(inventoryPojo);
    }

    public InventoryData getInventory(String barcode) throws ApiException {
        return inventoryFlow.getInventory(barcode);
    }

    public void updateInventory(InventoryForm inventoryForm, Long id) throws ApiException {
        InventoryPojo inventoryPojo = inventoryFlow.convert(inventoryForm);
        inventoryService.getCheck(id);
        inventoryService.updateInventory(id, inventoryPojo);
    }

    public void uploadInventory(MultipartFile file) throws IOException, ApiException {
        inventoryFlow.uploadInventory(file);
    }
}
