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
import com.increff.pos.util.TsvParserUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.*;

@Component
public class InventoryFlow {
    @Autowired
    private InventoryService inventoryService;

    @Autowired
    private ProductService productService;

    @Autowired
    private ClientService clientService;

    @Autowired
    private TsvParserUtil tsvParserUtil;

    public InventoryPojo getInventory(String barcode) throws ApiException {
        ProductPojo productPojo = productService.getProductByBarcode(barcode);
        inventoryService.getCheck(productPojo.getId());
        return inventoryService.getCheck(productPojo.getId());
    }

    public void uploadInventory(MultipartFile file, HttpServletResponse response) throws IOException, ApiException {
        List<InventoryPojo> validInventories = new ArrayList<>();
        List<Map<String, String>> errorRecords = new ArrayList<>();

        parseInventory(file, validInventories, errorRecords);
        saveValidInventories(validInventories, errorRecords, response);
    }

    private void parseInventory(MultipartFile file, List<InventoryPojo> validInventories, List<Map<String, String>> errorRecords) throws IOException {
        tsvParserUtil.parseTSV(file.getInputStream(),
                new HashSet<>(Arrays.asList("barcode", "quantity")),
                record -> {
                    Map<String, String> recordMap = new HashMap<>();
                    recordMap.put("barcode", record.get("barcode"));
                    recordMap.put("quantity", record.get("quantity"));

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
                        recordMap.put("error", e.getMessage());
                        errorRecords.add(recordMap);
                    }
                    return null;
                });
    }

    private void saveValidInventories(List<InventoryPojo> validInventories, List<Map<String, String>> errorRecords, HttpServletResponse response) throws ApiException, IOException {
        for (InventoryPojo inventory : validInventories) {
            inventoryService.addInventory(inventory);
        }

        if (!errorRecords.isEmpty()) {
            generateInventoryErrorTsv(errorRecords, response);
            throw new ApiException("Some inventory records failed - check downloaded error.tsv file");
        }
    }

    private void generateInventoryErrorTsv(List<Map<String, String>> errorRecords, HttpServletResponse response) throws IOException {
        response.setContentType("text/tab-separated-values");
        response.setHeader("Content-Disposition", "attachment; filename=error.tsv");

        try (PrintWriter writer = response.getWriter()) {
            writer.write("barcode\tquantity\terror\n");

            for (Map<String, String> record : errorRecords) {
                writer.write(String.format("%s\t%s\t%s\n",
                        tsvParserUtil.escapeTsv(record.get("barcode")),
                        tsvParserUtil.escapeTsv(record.get("quantity")),
                        tsvParserUtil.escapeTsv(record.get("error"))));
            }
        }
    }

    public String getBarcode(InventoryPojo inventoryPojo) {
        ProductPojo productPojo = productService.getCheck(inventoryPojo.getProdId());
        return productPojo.getBarcode();
    }
}
