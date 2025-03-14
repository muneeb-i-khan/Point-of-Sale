package com.increff.pos.flow;

import com.increff.pos.db.pojo.ClientPojo;
import com.increff.pos.db.pojo.InventoryPojo;
import com.increff.pos.db.pojo.ProductPojo;
import com.increff.pos.service.ClientService;
import com.increff.pos.service.InventoryService;
import com.increff.pos.service.ProductService;
import com.increff.pos.util.ApiException;
import com.increff.pos.util.Normalize;
import com.increff.pos.util.TsvParserUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.*;

@Service
public class TsvUploadFlow {

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

        parseProducts(file, validProducts, errorMessages);
        saveValidProducts(validProducts, errorMessages);
    }

    public void uploadInventory(MultipartFile file) throws IOException, ApiException {
        List<InventoryPojo> validInventories = new ArrayList<>();
        List<String> errorMessages = new ArrayList<>();

        parseInventory(file, validInventories, errorMessages);
        saveValidInventories(validInventories, errorMessages);
    }

    private void parseProducts(MultipartFile file, List<ProductPojo> validProducts, List<String> errorMessages) throws IOException {
        tsvParserUtil.parseTSV(file.getInputStream(),
                new HashSet<>(Arrays.asList("name", "barcode", "price", "clientName")),
                record -> {
                    try {
                        String barcode = record.get("barcode");
                        if (productService.getProductByBarcode(barcode) != null) {
                            throw new ApiException("Product with barcode '" + barcode + "' already exists.");
                        }

                        ProductPojo product = new ProductPojo();
                        product.setName(Normalize.normalizeName(record.get("name")));
                        product.setBarcode(barcode);
                        product.setPrice(Double.parseDouble(record.get("price")));

                        ClientPojo client = clientService.getCheck(record.get("clientName"));
                        product.setClientId(client.getId());

                        validProducts.add(product);
                    } catch (Exception e) {
                        errorMessages.add("Error in record: " + record.toString() + " - " + e.getMessage());
                    }
                    return null;
                });
    }

    private void parseInventory(MultipartFile file, List<InventoryPojo> validInventories, List<String> errorMessages) throws IOException {
        tsvParserUtil.parseTSV(file.getInputStream(),
                new HashSet<>(Arrays.asList("barcode", "quantity")),
                record -> {
                    try {
                        String barcode = record.get("barcode");
                        ProductPojo product = productService.getProductByBarcode(barcode);
                        if (product == null) {
                            throw new ApiException("Product not found with barcode: " + barcode);
                        }

                        InventoryPojo inventory = new InventoryPojo();
                        inventory.setProdId(product.getId());
                        inventory.setQuantity(Long.parseLong(record.get("quantity")));

                        validInventories.add(inventory);
                    } catch (Exception e) {
                        errorMessages.add("Error in record: " + record.toString() + " - " + e.getMessage());
                    }
                    return null;
                });
    }

    private void saveValidProducts(List<ProductPojo> validProducts, List<String> errorMessages) throws ApiException {
        for (ProductPojo product : validProducts) {
            productService.addProduct(product);
        }
        handleErrors(errorMessages);
    }

    private void saveValidInventories(List<InventoryPojo> validInventories, List<String> errorMessages) throws ApiException {
        for (InventoryPojo inventory : validInventories) {
            inventoryService.addInventory(inventory);
        }
        handleErrors(errorMessages);
    }

    private void handleErrors(List<String> errorMessages) throws ApiException {
        if (!errorMessages.isEmpty()) {
            throw new ApiException("Some records failed: " + String.join("; ", errorMessages));
        }
    }
}