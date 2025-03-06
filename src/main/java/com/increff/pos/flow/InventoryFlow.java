package com.increff.pos.flow;

import com.increff.pos.db.pojo.ClientPojo;
import com.increff.pos.db.pojo.InventoryPojo;
import com.increff.pos.db.pojo.ProductPojo;
import com.increff.pos.model.data.InventoryData;
import com.increff.pos.model.forms.InventoryForm;
import com.increff.pos.util.ApiException;
import com.increff.pos.service.ClientService;
import com.increff.pos.service.InventoryService;
import com.increff.pos.service.ProductService;
import com.increff.pos.service.TsvUploadService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Component
public class InventoryFlow {
    @Autowired
    private InventoryService inventoryService;

    @Autowired
    private ProductService productService;

    @Autowired
    private ClientService clientService;

    @Autowired
    private TsvUploadService tsvUploadService;

    public InventoryData getInventory(String barcode) throws ApiException {
        ProductPojo productPojo = productService.getProductByBarcode(barcode);
        inventoryService.getCheck(productPojo.getId());
        InventoryPojo inventoryPojo = inventoryService.getInventory(productPojo.getId());
        return convert(inventoryPojo);
    }

    public void uploadInventory(MultipartFile file) throws IOException, ApiException {
        tsvUploadService.uploadInventory(file);
    }

    public InventoryData convert(InventoryPojo inventoryPojo) throws ApiException {
        InventoryData inventoryData = new InventoryData();
        inventoryData.setId(inventoryPojo.getId());
        ProductPojo productPojo = productService.getProduct(inventoryPojo.getProdId());
        inventoryData.setBarcode(productPojo.getBarcode());
        inventoryData.setQuantity(inventoryPojo.getQuantity());
        inventoryData.setProdName(productPojo.getName());
        ClientPojo clientPojo = clientService.getClient(productPojo.getClientId());
        inventoryData.setClientName(clientPojo.getName());
        return inventoryData;
    }

    public InventoryPojo convert(InventoryForm inventoryForm) throws ApiException {
        InventoryPojo inventoryPojo = new InventoryPojo();
        ProductPojo productPojo = productService.getProductByBarcode(inventoryForm.getBarcode());
        inventoryPojo.setProdId(productPojo.getId());
        inventoryPojo.setQuantity(inventoryForm.getQuantity());
        return inventoryPojo;
    }
}
