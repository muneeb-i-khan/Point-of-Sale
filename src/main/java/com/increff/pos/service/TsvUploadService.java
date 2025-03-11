package com.increff.pos.service;

import com.increff.pos.db.pojo.ClientPojo;
import com.increff.pos.db.pojo.InventoryPojo;
import com.increff.pos.db.pojo.ProductPojo;
import com.increff.pos.util.ApiException;
import com.increff.pos.util.Normalize;
import com.increff.pos.util.TsvParserUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

@Service
public class TsvUploadService {

    @Autowired
    private TsvParserUtil tsvParserUtil;

    @Autowired
    private ProductService productService;

    @Autowired
    private InventoryService inventoryService;

    @Autowired
    private ClientService clientService;

    public void uploadProducts(MultipartFile file) throws IOException, ApiException {
        List<ProductPojo> validProducts = new ArrayList<>();
        List<String> errorMessages = new ArrayList<>();

        List<ProductPojo> products = tsvParserUtil.parseTSV(file.getInputStream(),
                new HashSet<>(Arrays.asList("name", "barcode", "price", "clientName")),
                record -> {
                    ProductPojo p = new ProductPojo();
                    try {
                        String barcode = record.get("barcode");

                        if (productService.getProductByBarcode(barcode) != null) {
                            throw new ApiException("Product with barcode '" + barcode + "' already exists.");
                        }

                        p.setName(Normalize.normalizeName(record.get("name")));
                        p.setBarcode(barcode);
                        p.setPrice(Double.parseDouble(record.get("price")));

                        ClientPojo cp = clientService.getCheck(record.get("clientName"));
                        p.setClientId(cp.getId());

                        validProducts.add(p);
                    } catch (Exception e) {
                        errorMessages.add("Error in record: " + record.toString() + " - " + e.getMessage());
                    }
                    return null;
                });

        for (ProductPojo product : validProducts) {
            productService.addProduct(product);
        }

        if (!errorMessages.isEmpty()) {
            throw new ApiException("Some records failed: " + String.join("; ", errorMessages));
        }
    }

    public void uploadInventory(MultipartFile file) throws IOException, ApiException {
        List<InventoryPojo> validInventories = new ArrayList<>();
        List<String> errorMessages = new ArrayList<>();

        List<InventoryPojo> inventoryList = tsvParserUtil.parseTSV(file.getInputStream(),
                new HashSet<>(Arrays.asList("barcode", "quantity")),
                record -> {
                    try {
                        String barcode = record.get("barcode");

                        ProductPojo productPojo = productService.getProductByBarcode(barcode);
                        if (productPojo == null) {
                            throw new ApiException("Product not found with barcode: " + barcode);
                        }

                        InventoryPojo inventoryPojo = new InventoryPojo();
                        inventoryPojo.setProdId(productPojo.getId());
                        inventoryPojo.setQuantity(Long.parseLong(record.get("quantity")));

                        validInventories.add(inventoryPojo);
                    } catch (Exception e) {
                        errorMessages.add("Error in record: " + record.toString() + " - " + e.getMessage());
                    }
                    return null;
                });

        for (InventoryPojo inventoryPojo : validInventories) {
            inventoryService.addInventory(inventoryPojo);
        }

        if (!errorMessages.isEmpty()) {
            throw new ApiException("Some records failed: " + String.join("; ", errorMessages));
        }
    }

}
