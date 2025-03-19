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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
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
    private TsvUploadFlow tsvUploadFlow;

    public InventoryData getInventory(String barcode) throws ApiException {
        ProductPojo productPojo = productService.getProductByBarcode(barcode);
        inventoryService.getCheck(productPojo.getId());
        InventoryPojo inventoryPojo = inventoryService.getCheck(productPojo.getId());
        return convert(inventoryPojo);
    }

    public void uploadInventory(MultipartFile file, HttpServletResponse response) throws IOException, ApiException {
        tsvUploadFlow.uploadInventory(file, response);
    }

    public String getBarcode(InventoryPojo inventoryPojo) {
        ProductPojo productPojo = productService.getCheck(inventoryPojo.getProdId());
        return productPojo.getBarcode();
    }

    public InventoryPojo convert(InventoryForm inventoryForm) throws ApiException {
        InventoryPojo inventoryPojo = new InventoryPojo();
        ProductPojo productPojo = productService.getProductByBarcode(inventoryForm.getBarcode());
        inventoryPojo.setProdId(productPojo.getId());
        inventoryPojo.setQuantity(inventoryForm.getQuantity());
        return inventoryPojo;
    }

    public InventoryData convert(InventoryPojo inventoryPojo) throws ApiException {
        InventoryData inventoryData = new InventoryData();
        inventoryData.setId(inventoryPojo.getId());
        ProductPojo productPojo = productService.getCheck(inventoryPojo.getProdId());
        inventoryData.setBarcode(productPojo.getBarcode());
        inventoryData.setQuantity(inventoryPojo.getQuantity());
        inventoryData.setProdName(productPojo.getName());
        ClientPojo clientPojo = clientService.getCheck(productPojo.getClientId());
        inventoryData.setClientName(clientPojo.getName());
        return inventoryData;
    }
}
