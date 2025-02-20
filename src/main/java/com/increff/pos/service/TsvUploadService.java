package com.increff.pos.service;

import com.increff.pos.db.pojo.ClientPojo;
import com.increff.pos.db.pojo.InventoryPojo;
import com.increff.pos.db.pojo.ProductPojo;
import com.increff.pos.util.Normalize;
import com.increff.pos.util.TsvParserUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

@Service
public class TsvUploadService {

    @Autowired
    private ProductService productService;

    @Autowired
    private InventoryService inventoryService;

    @Autowired
    private ClientService clientService;

    public void uploadProducts(MultipartFile file) throws IOException, ApiException {
        List<ProductPojo> products = TsvParserUtil.parseTSV(file.getInputStream(),
                new HashSet<>(Arrays.asList("name", "barcode", "price", "clientName")),
                record -> {
                    ProductPojo p = new ProductPojo();
                    p.setName(Normalize.normalizeName(record.get("name")));
                    p.setBarcode(record.get("barcode"));
                    p.setPrice(Double.parseDouble(record.get("price")));

                    try {
                        ClientPojo cp = clientService.getClientByName(record.get("clientName"));
                        p.setClientPojo(cp);
                    } catch (ApiException e) {
                        throw new RuntimeException("Client not found: " + record.get("clientName"));
                    }

                    return p;
                });

        for (ProductPojo product : products) {
            productService.addProduct(product);
        }
    }

    public void uploadInventory(MultipartFile file) throws IOException, ApiException {
        List<InventoryPojo> inventoryList = TsvParserUtil.parseTSV(file.getInputStream(),
                new HashSet<>(Arrays.asList("barcode", "quantity")),
                record -> {
                    InventoryPojo inventoryPojo = new InventoryPojo();
                    inventoryPojo.setBarcode(record.get("barcode"));
                    inventoryPojo.setQuantity(Long.parseLong(record.get("quantity")));

                    try {
                        ProductPojo productPojo = productService.getProductByBarcode(inventoryPojo.getBarcode());
                        inventoryPojo.setProductPojo(productPojo);
                    } catch (ApiException e) {
                        throw new RuntimeException("Product not found for barcode: " + inventoryPojo.getBarcode());
                    }

                    return inventoryPojo;
                });

        for (InventoryPojo inventoryPojo : inventoryList) {
            inventoryService.addInventory(inventoryPojo);
        }
    }

}
