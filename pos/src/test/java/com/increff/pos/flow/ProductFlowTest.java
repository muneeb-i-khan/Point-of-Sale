package com.increff.pos.flow;

import com.increff.pos.db.pojo.ClientPojo;
import com.increff.pos.db.pojo.ProductPojo;
import com.increff.pos.model.data.ProductData;
import com.increff.pos.model.forms.ProductForm;
import com.increff.pos.service.*;
import com.increff.pos.util.ApiException;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

import static org.junit.Assert.*;

@ContextConfiguration(classes = QaConfig.class)
@Transactional
@Rollback
public class ProductFlowTest extends AbstractUnitTest {

    @Autowired
    private ProductFlow productFlow;

    @Autowired
    private ProductService productService;

    @Autowired
    private ClientService clientService;

    private ClientPojo client;

    @Before
    public void setUp() throws ApiException {
        client = new ClientPojo();
        client.setName("Test Client");
        clientService.addClient(client);
    }

    @Test
    public void testAddProduct_Success() throws ApiException {
        ProductForm form = new ProductForm();
        form.setName("Test Product");
        form.setBarcode("123456");
        form.setPrice(100.0);
        form.setClientName(client.getName());

        productFlow.addProduct(form);

        ProductPojo product = productService.getProductByBarcode("123456");
        assertNotNull(product);
        assertEquals("Test Product", product.getName());
        assertEquals(100.0, product.getPrice(), 0.01);
        assertEquals(client.getId(), product.getClientId());
    }

    @Test(expected = ApiException.class)
    public void testAddProduct_ClientNotFound() throws ApiException {
        ProductForm form = new ProductForm();
        form.setName("Invalid Product");
        form.setBarcode("999999");
        form.setPrice(50.0);
        form.setClientName("Nonexistent Client");

        productFlow.addProduct(form);
    }

//    @Test
//    public void testUploadProducts_Success() throws IOException, ApiException {
//        String tsvData = "name\tbarcode\tprice\tclientName\n" +
//                "Product1\t111111\t50\tTest Client\n" +
//                "Product2\t222222\t75\tTest Client\n";
//
//        MockMultipartFile file = new MockMultipartFile(
//                "file", "products.tsv", "text/plain", tsvData.getBytes(StandardCharsets.UTF_8));
//
//        productFlow.uploadProducts(file);
//
//        ProductPojo product1 = productService.getProductByBarcode("111111");
//        ProductPojo product2 = productService.getProductByBarcode("222222");
//
//        assertNotNull(product1);
//        assertEquals("product1", product1.getName());
//        assertEquals(50.0, product1.getPrice(), 0.01);
//
//        assertNotNull(product2);
//        assertEquals("product2", product2.getName());
//        assertEquals(75.0, product2.getPrice(), 0.01);
//    }

    @Test
    public void testConvertProduct_Success() throws ApiException {
        ProductPojo product = new ProductPojo();
        product.setId(1L);
        product.setName("Product X");
        product.setBarcode("987654");
        product.setPrice(200.0);
        product.setClientId(client.getId());

        ProductData productData = productFlow.convert(product);

        assertEquals("product x", productData.getName());
        assertEquals("987654", productData.getBarcode());
        assertEquals(200.0, productData.getPrice(), 0.01);
        assertEquals(client.getName(), productData.getClientName());
    }
}
