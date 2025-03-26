package com.increff.pos.flow;

import com.increff.pos.db.pojo.ClientPojo;
import com.increff.pos.db.pojo.ProductPojo;
import com.increff.pos.spring.ApplicationProperties;
import com.increff.pos.util.ApiException;
import com.increff.pos.service.ProductService;
import com.increff.pos.service.ClientService;
import com.increff.pos.util.Normalize;
import com.increff.pos.util.TsvParserUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.*;

@Component
public class ProductFlow {
    @Autowired
    private ProductService productService;

    @Autowired
    private ClientService clientService;

    @Autowired
    private ApplicationProperties applicationProperties;

    public ProductPojo addProduct(ProductPojo productPojo, String clientName) throws ApiException {
        ClientPojo clientPojo;
        clientPojo = clientService.getCheck(clientName);
        productPojo.setClientId(clientPojo.getId());
        productService.addProduct(productPojo);
        return productPojo;
    }

    public void uploadProducts(MultipartFile file, HttpServletResponse response) throws IOException, ApiException {
        List<ProductPojo> validProducts = new ArrayList<>();
        List<Map<String, String>> errorRecords = new ArrayList<>();

        parseProducts(file, validProducts, errorRecords);
        saveValidProducts(validProducts, errorRecords, response);
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


    private void parseProducts(MultipartFile file, List<ProductPojo> validProducts, List<Map<String, String>> errorRecords) throws IOException {
        TsvParserUtil.parseTSV(file.getInputStream(),
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
                },applicationProperties.getMaxTsvLines());
    }

    private void generateErrorTsv(List<Map<String, String>> errorRecords, HttpServletResponse response) throws IOException {
        response.setContentType("text/tab-separated-values");
        response.setHeader("Content-Disposition", "attachment; filename=error.tsv");

        try (PrintWriter writer = response.getWriter()) {
            writer.write("name\tbarcode\tprice\tclientName\terror\n");

            for (Map<String, String> record : errorRecords) {
                writer.write(String.format("%s\t%s\t%s\t%s\t%s\n",
                        TsvParserUtil.escapeTsv(record.get("name")),
                        TsvParserUtil.escapeTsv(record.get("barcode")),
                        TsvParserUtil.escapeTsv(record.get("price")),
                        TsvParserUtil.escapeTsv(record.get("clientName")),
                        TsvParserUtil.escapeTsv(record.get("error"))));
            }
        }
    }

}
