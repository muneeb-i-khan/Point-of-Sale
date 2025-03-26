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
        ProductPojo pojo = new ProductPojo();
        pojo.setName("Test Product");
        pojo.setBarcode("123456");
        pojo.setPrice(100.0);

        productFlow.addProduct(pojo,"Test Client");

        ProductPojo product = productService.getProductByBarcode("123456");
        assertNotNull(product);
        assertEquals("Test Product", product.getName());
        assertEquals(100.0, product.getPrice(), 0.01);
        assertEquals(client.getId(), product.getClientId());
    }

    @Test(expected = ApiException.class)
    public void testAddProduct_ClientNotFound() throws ApiException {
        ProductPojo pojo = new ProductPojo();
        pojo.setName("Invalid Product");
        pojo.setBarcode("999999");
        pojo.setPrice(50.0);

        productFlow.addProduct(pojo, "Non Existant Client");
    }

}
