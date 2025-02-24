package com.increff.pos.dto;

import com.increff.pos.db.pojo.ClientPojo;
import com.increff.pos.db.pojo.InventoryPojo;
import com.increff.pos.db.pojo.ProductPojo;
import com.increff.pos.model.data.InventoryData;
import com.increff.pos.model.forms.InventoryForm;
import com.increff.pos.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class InventoryDto {
    @Autowired
    ProductService productService;
    @Autowired
    ClientService clientService;
    @Autowired
    InventoryService inventoryService;

//    @Autowired
//    TsvUploadService tsvUploadService;

    public void addInventory(InventoryForm inventoryForm) throws ApiException {
        InventoryPojo inventoryPojo = convert(inventoryForm);
        inventoryService.addInventory(inventoryPojo);
    }

    public List<InventoryData> getAllInventories() throws ApiException {
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

//    public void deleteInventory(Long id) throws ApiException {
//        inventoryService.getCheck(id);
//        inventoryService.deleteInventory(id);
//    }

//    public void uploadInventory(MultipartFile file) throws IOException, ApiException {
//        tsvUploadService.uploadInventory(file);
//    }

    public InventoryData convert(InventoryPojo inventoryPojo) throws ApiException {
        InventoryData inventoryData = new InventoryData();
        inventoryData.setId(inventoryPojo.getId());
//        inventoryData.setBarcode(inventoryPojo.getBarcode());
        ProductPojo productPojo = productService.getProduct(inventoryPojo.getProd_id());
        inventoryData.setBarcode(productPojo.getBarcode());
        inventoryData.setQuantity(inventoryPojo.getQuantity());
        inventoryData.setProdName(productPojo.getName());
        ClientPojo clientPojo = clientService.getClient(productPojo.getClient_id());
        inventoryData.setClientName(clientPojo.getName());
//        inventoryData.setProdName(inventoryPojo.getProductPojo().getName());
//        inventoryData.setProdId(inventoryPojo.getProductPojo().getId());
//        inventoryData.setClientName(inventoryPojo.getProductPojo().getClientPojo().getName());
        return inventoryData;
    }

    public InventoryPojo convert(InventoryForm inventoryForm) throws ApiException {
        InventoryPojo inventoryPojo = new InventoryPojo();
//        inventoryPojo.setBarcode(inventoryForm.getBarcode());
        ProductPojo productPojo = productService.getProductByBarcode(inventoryForm.getBarcode());
        inventoryPojo.setProd_id(productPojo.getId());
        inventoryPojo.setQuantity(inventoryForm.getQuantity());
//        ProductPojo productPojo = productService.getProductByBarcode(inventoryForm.getBarcode());
//        inventoryPojo.setProductPojo(productPojo);
        return inventoryPojo;
    }

}
