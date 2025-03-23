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

}
