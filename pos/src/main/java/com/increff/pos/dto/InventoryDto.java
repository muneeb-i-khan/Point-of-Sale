package com.increff.pos.dto;

import com.increff.pos.db.pojo.InventoryPojo;
import com.increff.pos.model.data.InventoryData;
import com.increff.pos.model.forms.InventoryForm;
import com.increff.pos.util.ApiException;
import com.increff.pos.service.InventoryService;
import com.increff.pos.flow.InventoryFlow;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Component
public class InventoryDto {
    @Autowired
    private InventoryService inventoryService;

    @Autowired
    private InventoryFlow inventoryFlow;

    public InventoryData addInventory(InventoryForm inventoryForm) throws ApiException {
        InventoryPojo inventoryPojo = inventoryFlow.convert(inventoryForm);
        inventoryService.addInventory(inventoryPojo);
        return inventoryFlow.convert(inventoryPojo);
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
        InventoryPojo inventoryPojo = inventoryService.getCheck(id);
        return inventoryFlow.convert(inventoryPojo);
    }

    public InventoryData getInventory(String barcode) throws ApiException {
        return inventoryFlow.getInventory(barcode);
    }


    public List<InventoryData> getAllInventoriesPaginated(int page, int pageSize, HttpServletResponse httpServletResponse) throws ApiException {
        List<InventoryPojo> inventoryPojos = inventoryService.getAllInventoriesPaginated(page, pageSize);
        Long totalInventories = inventoryService.getInventoryCount();

        List<InventoryData> inventoryDataList = new ArrayList<>();
        for (InventoryPojo p : inventoryPojos) {
            inventoryDataList.add(inventoryFlow.convert(p));
        }

        httpServletResponse.setHeader("totalInventories", totalInventories.toString());
        return inventoryDataList;
    }
    

    public InventoryData updateInventory(InventoryForm inventoryForm, Long id) throws ApiException {
        InventoryPojo inventoryPojo = inventoryFlow.convert(inventoryForm);
        inventoryService.getCheck(id);
        inventoryService.updateInventory(id, inventoryPojo);
        return inventoryFlow.convert(inventoryPojo);
    }

    public void uploadInventory(MultipartFile file, HttpServletResponse response) throws IOException, ApiException {
        inventoryFlow.uploadInventory(file, response);
    }
}
