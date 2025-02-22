package com.increff.pos.dto;

import com.increff.pos.db.pojo.ClientPojo;
import com.increff.pos.model.data.ProductData;
import com.increff.pos.model.forms.ProductForm;
import com.increff.pos.service.*;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static org.junit.Assert.*;

import java.util.List;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = QaConfig.class)
public class ProductDtoTest extends AbstractUnitTest {

    @Autowired
    private ProductDto productDto;

    @Autowired
    private ProductService productService;

    @Autowired
    private ClientService clientService;

    private Long clientId;

    @Before
    public void setUp() throws ApiException {
        ClientPojo client = new ClientPojo();
        client.setName("Test Client");
        clientService.addClient(client);
        clientId = client.getId();
    }

    @Test
    public void testAddProduct() throws ApiException {
        ProductForm form = new ProductForm();
        form.setName("Test Product");
        form.setBarcode("123456");
        form.setPrice(100.0);
        form.setClientName("Test Client");

        productDto.addProduct(form);

        List<ProductData> products = productDto.getAllProducts();
        assertEquals(1, products.size());
        assertEquals("test product", products.get(0).getName());
    }

    @Test
    public void testGetProduct() throws ApiException {
        ProductForm form = new ProductForm();
        form.setName("Product A");
        form.setBarcode("7891011");
        form.setPrice(150.0);
        form.setClientName("Test Client");

        productDto.addProduct(form);

        List<ProductData> products = productDto.getAllProducts();
        ProductData product = productDto.getProduct(products.get(0).getId());

        assertNotNull(product);
        assertEquals("product a", product.getName());
        assertEquals("7891011", product.getBarcode());
    }

    @Test
    public void testUpdateProduct() throws ApiException {
        ProductForm form = new ProductForm();
        form.setName("Old Name");
        form.setBarcode("111222");
        form.setPrice(200.0);
        form.setClientName("Test Client");

        productDto.addProduct(form);

        List<ProductData> products = productDto.getAllProducts();
        Long productId = products.get(0).getId();

        ProductForm updateForm = new ProductForm();
        updateForm.setName("Updated Name");
        updateForm.setBarcode("111222");
        updateForm.setPrice(250.0);
        updateForm.setClientName("Test Client");

        productDto.updateProduct(productId, updateForm);

        ProductData updatedProduct = productDto.getProduct(productId);
        assertEquals("updated name", updatedProduct.getName());
        assertEquals(250.0, updatedProduct.getPrice(), 0.01);
    }

    @Test
    public void testDeleteProduct() throws ApiException {
        ProductForm form = new ProductForm();
        form.setName("To Be Deleted");
        form.setBarcode("999888");
        form.setPrice(50.0);
        form.setClientName("Test Client");

        productDto.addProduct(form);

        List<ProductData> products = productDto.getAllProducts();
        Long productId = products.get(0).getId();

        productDto.deleteProduct(productId);

        List<ProductData> updatedProducts = productDto.getAllProducts();
        assertEquals(0, updatedProducts.size());
    }
}
