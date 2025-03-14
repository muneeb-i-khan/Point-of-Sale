//package com.increff.pos.service;
//
//import com.increff.pos.db.pojo.ClientPojo;
//import com.increff.pos.db.pojo.InventoryPojo;
//import com.increff.pos.db.pojo.ProductPojo;
//import com.increff.pos.util.ApiException;
//import org.junit.Before;
//import org.junit.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.mock.web.MockMultipartFile;
//import org.springframework.test.annotation.Rollback;
//import org.springframework.test.context.ContextConfiguration;
//import org.springframework.transaction.annotation.Transactional;
//
//import java.io.IOException;
//import java.nio.charset.StandardCharsets;
//import java.util.List;
//
//import static org.junit.Assert.*;
//
//@ContextConfiguration(classes = QaConfig.class)
//@Transactional
//@Rollback
//public class TsvUploadServiceTest extends AbstractUnitTest {
//
//    @Autowired
//    private TsvUploadService tsvUploadService;
//
//    @Autowired
//    private ProductService productService;
//
//    @Autowired
//    private InventoryService inventoryService;
//
//    @Autowired
//    private ClientService clientService;
//
//    private ClientPojo client;
//
//    @Before
//    public void setUp() throws ApiException {
//        client = new ClientPojo();
//        client.setName("Test Client");
//        client.setDescription("Client for testing");
//        clientService.addClient(client);
//    }
//
//    @Test
//    public void testUploadProducts_Success() throws IOException, ApiException {
//        String tsvData = "name\tbarcode\tprice\tclientName\n" +
//                "Test Product\t123456\t50.0\tTest Client\n";
//
//        MockMultipartFile file = new MockMultipartFile(
//                "file", "products.tsv", "text/plain",
//                tsvData.getBytes(StandardCharsets.UTF_8));
//
//        tsvUploadService.uploadProducts(file);
//
//        List<ProductPojo> products = productService.getAllProducts();
//        assertEquals(1, products.size());
//
//        ProductPojo product = products.get(0);
//        assertEquals("test product", product.getName());
//        assertEquals("123456", product.getBarcode());
//        assertEquals(50.0, product.getPrice(), 0.01);
//        assertEquals(client.getId(), product.getClientId());
//    }
//
//    @Test(expected = RuntimeException.class)
//    public void testUploadProducts_ClientNotFound() throws IOException, ApiException {
//        String tsvData = "name\tbarcode\tprice\tclientName\n" +
//                "Test Product\t123456\t50.0\tNon-Existent Client\n";
//
//        MockMultipartFile file = new MockMultipartFile(
//                "file", "products.tsv", "text/plain",
//                tsvData.getBytes(StandardCharsets.UTF_8));
//
//        tsvUploadService.uploadProducts(file);
//    }
//
//    @Test
//    public void testUploadInventory_Success() throws IOException, ApiException {
//        ProductPojo product = new ProductPojo();
//        product.setName("Test Product");
//        product.setBarcode("123456");
//        product.setPrice(50.0);
//        product.setClientId(client.getId());
//        productService.addProduct(product);
//
//        String tsvData = "barcode\tquantity\n" +
//                "123456\t100\n";
//
//        MockMultipartFile file = new MockMultipartFile(
//                "file", "inventory.tsv", "text/plain",
//                    tsvData.getBytes(StandardCharsets.UTF_8));
//
//        tsvUploadService.uploadInventory(file);
//
//        InventoryPojo inventory = inventoryService.getInventoryByBarcode("123456");
//        assertNotNull(inventory);
//        assertEquals(100, inventory.getQuantity().intValue());
//    }
//
//    @Test(expected = RuntimeException.class)
//    public void testUploadInventory_ProductNotFound() throws IOException, ApiException {
//        String tsvData = "barcode\tquantity\n" +
//                "999999\t100\n";
//
//        MockMultipartFile file = new MockMultipartFile(
//                "file", "inventory.tsv", "text/plain",
//                tsvData.getBytes(StandardCharsets.UTF_8));
//
//        tsvUploadService.uploadInventory(file);
//    }
//
//    @Test(expected = IllegalArgumentException.class)
//    public void testUploadInventoryProductsWrongHeader() throws ApiException, IOException {
//        ProductPojo product = new ProductPojo();
//        product.setName("Test Product");
//        product.setBarcode("123456");
//        product.setPrice(50.0);
//        product.setClientId(client.getId());
//        productService.addProduct(product);
//
//        String tsvData = "barcode\tprice\n" +
//                "123456\t100\n";
//
//        MockMultipartFile file = new MockMultipartFile(
//                "file", "inventory.tsv", "text/plain",
//                tsvData.getBytes(StandardCharsets.UTF_8));
//
//        tsvUploadService.uploadInventory(file);
//    }
//
//    @Test(expected = IllegalArgumentException.class)
//    public void testUploadProductsWrongHeader() throws ApiException, IOException {
//        String tsvData = "product_name\tbarcode\tcost\tclient_title\n" +
//                "Test Product\t123456\t50.0\tTest Client\n";  // Incorrect headers
//
//        MockMultipartFile file = new MockMultipartFile(
//                "file", "products.tsv", "text/plain",
//                tsvData.getBytes(StandardCharsets.UTF_8));
//
//        tsvUploadService.uploadProducts(file);
//    }
//}
