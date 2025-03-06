package com.increff.pos.service;

import static org.junit.Assert.*;

import com.increff.pos.db.pojo.ClientPojo;
import com.increff.pos.db.pojo.ProductPojo;
import com.increff.pos.util.ApiException;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@ContextConfiguration(classes = QaConfig.class)
@Transactional
@Rollback
public class ProductServiceTest extends AbstractUnitTest {

    @Autowired
    private ProductService productService;

    @Autowired
    private ClientService clientService;

    @Test
    public void addProductTest() throws ApiException {
        ClientPojo client = new ClientPojo();
        client.setName("Samsung");
        client.setDescription("Electronics");
        clientService.addClient(client);
        ClientPojo savedClient = clientService.getClientByName("Samsung");

        ProductPojo product = new ProductPojo();
        product.setBarcode("12345");
        product.setName("Galaxy S21");
        product.setClientId(savedClient.getId());
        productService.addProduct(product);

        ProductPojo retrievedProduct = productService.getProductByBarcode("12345");
        assertNotNull(retrievedProduct);
        assertEquals("Galaxy S21", retrievedProduct.getName());
        assertEquals("Samsung", clientService.getClient(product.getClientId()).getName());
    }

    @Test
    public void getProductByIdTest() throws ApiException {
        ClientPojo client = new ClientPojo();
        client.setName("Apple");
        client.setDescription("Technology");
        clientService.addClient(client);
        ClientPojo savedClient = clientService.getClientByName("Apple");

        ProductPojo product = new ProductPojo();
        product.setBarcode("A123");
        product.setName("iPhone 15");
        product.setClientId(savedClient.getId());
        productService.addProduct(product);
        ProductPojo savedProduct = productService.getProductByBarcode("A123");

        ProductPojo retrievedProduct = productService.getProduct(savedProduct.getId());

        assertNotNull(retrievedProduct);
        assertEquals("iPhone 15", retrievedProduct.getName());
        assertEquals("A123", retrievedProduct.getBarcode());
    }

    @Test(expected = ApiException.class)
    public void getProductByIdNotFoundTest() throws ApiException {
        productService.getProduct(999L);
    }

    @Test
    public void getAllProductsTest() throws ApiException {
        ClientPojo client1 = new ClientPojo();
        client1.setName("Sony");
        client1.setDescription("Entertainment");
        clientService.addClient(client1);
        ClientPojo savedClient1 = clientService.getClientByName("Sony");

        ClientPojo client2 = new ClientPojo();
        client2.setName("LG");
        client2.setDescription("Appliances");
        clientService.addClient(client2);
        ClientPojo savedClient2 = clientService.getClientByName("LG");

        ProductPojo p1 = new ProductPojo();
        p1.setBarcode("SON123");
        p1.setName("PlayStation 5");
        p1.setPrice(45000);
        p1.setClientId(savedClient1.getId());
        ProductPojo p2 = new ProductPojo();
        p2.setBarcode("LG456");
        p2.setName("LG OLED TV");
        p2.setPrice(65000);
        p2.setClientId(savedClient2.getId());
        productService.addProduct(p1);
        productService.addProduct(p2);

        List<ProductPojo> products = productService.getAllProducts();
        assertEquals(2, products.size());
    }

    @Test
    public void updateProductTest() throws ApiException {
        ClientPojo client = new ClientPojo();
        client.setName("OnePlus");
        client.setDescription("Mobiles");
        clientService.addClient(client);
        ClientPojo savedClient = clientService.getClientByName("OnePlus");

        ProductPojo product = new ProductPojo();
        product.setBarcode("OP123");
        product.setName("OnePlus 9");
        product.setPrice(3200);
        product.setClientId(savedClient.getId());
        productService.addProduct(product);

        ProductPojo savedProduct = productService.getProductByBarcode("OP123");

        ProductPojo updatedProduct = new ProductPojo();
        updatedProduct.setBarcode("OP999");
        updatedProduct.setName("OnePlus 10");
        updatedProduct.setPrice(4000);
        updatedProduct.setClientId(savedProduct.getClientId());
        productService.updateProduct(savedProduct.getId(), updatedProduct);

        ProductPojo result = productService.getProduct(savedProduct.getId());
        assertEquals("OnePlus 10", result.getName());
        assertEquals("OP999", result.getBarcode());
        assertEquals(4000.0, result.getPrice(), 0.00001);
    }

    @Test(expected = ApiException.class)
    public void updateProductNotFoundTest() throws ApiException {
        ProductPojo updatePojo = new ProductPojo();
        updatePojo.setBarcode("XYZ123");
        updatePojo.setName("NonExistent");
        updatePojo.setPrice(1000);

        productService.updateProduct(999L, updatePojo);
    }
}
