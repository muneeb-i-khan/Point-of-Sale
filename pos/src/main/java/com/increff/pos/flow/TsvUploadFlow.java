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

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
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

    public void uploadProducts(MultipartFile file, HttpServletResponse response) throws IOException, ApiException {
        List<ProductPojo> validProducts = new ArrayList<>();
        List<Map<String, String>> errorRecords = new ArrayList<>();

        parseProducts(file, validProducts, errorRecords);
        saveValidProducts(validProducts, errorRecords, response);
    }

    public void uploadInventory(MultipartFile file, HttpServletResponse response) throws IOException, ApiException {
        List<InventoryPojo> validInventories = new ArrayList<>();
        List<Map<String, String>> errorRecords = new ArrayList<>();

        parseInventory(file, validInventories, errorRecords);
        saveValidInventories(validInventories, errorRecords, response);
    }

    private void parseProducts(MultipartFile file, List<ProductPojo> validProducts, List<Map<String, String>> errorRecords) throws IOException {
        tsvParserUtil.parseTSV(file.getInputStream(),
                new HashSet<>(Arrays.asList("name", "barcode", "price", "clientName")),
                record -> {
                    Map<String, String> recordMap = new HashMap<>();
                    recordMap.put("name", record.get("name"));
                    recordMap.put("barcode", record.get("barcode"));
                    recordMap.put("price", record.get("price"));
                    recordMap.put("clientName", record.get("clientName"));

                    try {
                        String barcode = record.get("barcode");
                        if (productService.getCheckBarcode(barcode) != null) {
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
                        recordMap.put("error", e.getMessage());
                        errorRecords.add(recordMap);
                    }
                    return null;
                });
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

    private void saveValidProducts(List<ProductPojo> validProducts, List<Map<String, String>> errorRecords, HttpServletResponse response) throws ApiException, IOException {
        for (ProductPojo product : validProducts) {
            productService.addProduct(product);
        }

        if (!errorRecords.isEmpty()) {
            generateErrorTsv(errorRecords, response);
            throw new ApiException("Some records failed - check downloaded error.tsv file");
        }
    }

    private void generateErrorTsv(List<Map<String, String>> errorRecords, HttpServletResponse response) throws IOException {
        response.setContentType("text/tab-separated-values");
        response.setHeader("Content-Disposition", "attachment; filename=error.tsv");

        try (PrintWriter writer = response.getWriter()) {
            writer.write("name\tbarcode\tprice\tclientName\terror\n");

            for (Map<String, String> record : errorRecords) {
                writer.write(String.format("%s\t%s\t%s\t%s\t%s\n",
                        escapeTsv(record.get("name")),
                        escapeTsv(record.get("barcode")),
                        escapeTsv(record.get("price")),
                        escapeTsv(record.get("clientName")),
                        escapeTsv(record.get("error"))));
            }
        }
    }

    private String escapeTsv(String value) {
        if (value == null) return "";
        return value.replace("\t", "\\t").replace("\n", "\\n");
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
                        escapeTsv(record.get("barcode")),
                        escapeTsv(record.get("quantity")),
                        escapeTsv(record.get("error"))));
            }
        }
    }
}