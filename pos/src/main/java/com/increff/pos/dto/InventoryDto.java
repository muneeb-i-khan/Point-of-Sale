package com.increff.pos.dto;

import com.increff.pos.db.pojo.ClientPojo;
import com.increff.pos.db.pojo.InventoryPojo;
import com.increff.pos.db.pojo.ProductPojo;
import com.increff.pos.model.data.InventoryData;
import com.increff.pos.model.forms.InventoryForm;
import com.increff.pos.service.ClientService;
import com.increff.pos.service.ProductService;
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
    private ClientService clientService;

    @Autowired
    private ProductService productService;

    @Autowired
    private InventoryFlow inventoryFlow;

    public InventoryData addInventory(InventoryForm inventoryForm) throws ApiException {
        InventoryPojo inventoryPojo = convert(inventoryForm);
        inventoryService.addInventory(inventoryPojo);
        return convert(inventoryPojo);
    }

    public List<InventoryData> getAllInventories() throws ApiException {
        List<InventoryPojo> list = inventoryService.getAllInventories();
        List<InventoryData> list2 = new ArrayList<>();
        for(InventoryPojo p : list) {
            list2.add(convert(p));
        }
        return list2;
    }

    public InventoryData getInventory(Long id) throws ApiException {
        inventoryService.getCheck(id);
        InventoryPojo inventoryPojo = inventoryService.getCheck(id);
        return convert(inventoryPojo);
    }

    public InventoryData getInventory(String barcode) throws ApiException {
        return convert(inventoryFlow.getInventory(barcode));
    }


    public List<InventoryData> getAllInventoriesPaginated(int page, int pageSize, HttpServletResponse httpServletResponse) throws ApiException {
        List<InventoryPojo> inventoryPojos = inventoryService.getAllInventoriesPaginated(page, pageSize);
        Long totalInventories = inventoryService.getInventoryCount();

        List<InventoryData> inventoryDataList = new ArrayList<>();
        for (InventoryPojo p : inventoryPojos) {
            inventoryDataList.add(convert(p));
        }

        httpServletResponse.setHeader("totalInventories", totalInventories.toString());
        return inventoryDataList;
    }
    

    public InventoryData updateInventory(InventoryForm inventoryForm, Long id) throws ApiException {
        InventoryPojo inventoryPojo = convert(inventoryForm);
        inventoryService.getCheck(id);
        inventoryService.updateInventory(id, inventoryPojo);
        return convert(inventoryPojo);
    }

    public void uploadInventory(MultipartFile file, HttpServletResponse response) throws IOException, ApiException {
        inventoryFlow.uploadInventory(file, response);
    }

    private InventoryData convert(InventoryPojo inventoryPojo) throws ApiException {
        ProductPojo productPojo = productService.getCheck(inventoryPojo.getProdId());
        ClientPojo clientPojo = clientService.getCheck(productPojo.getClientId());

        InventoryData inventoryData = new InventoryData();
        inventoryData.setId(inventoryPojo.getId());
        inventoryData.setBarcode(productPojo.getBarcode());
        inventoryData.setQuantity(inventoryPojo.getQuantity());
        inventoryData.setProdName(productPojo.getName());
        inventoryData.setClientName(clientPojo.getName());

        return inventoryData;
    }

    private InventoryPojo convert(InventoryForm inventoryForm) throws ApiException {
        InventoryPojo inventoryPojo = new InventoryPojo();
        ProductPojo productPojo = productService.getProductByBarcode(inventoryForm.getBarcode());

        inventoryPojo.setProdId(productPojo.getId());
        inventoryPojo.setQuantity(inventoryForm.getQuantity());

        return inventoryPojo;
    }

}
