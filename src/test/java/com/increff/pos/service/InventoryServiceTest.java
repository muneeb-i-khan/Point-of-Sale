package com.increff.pos.service;

import static org.junit.Assert.*;

import com.increff.pos.db.pojo.InventoryPojo;
import com.increff.pos.db.pojo.ProductPojo;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = QaConfig.class)
@Transactional
@Rollback
public class InventoryServiceTest extends AbstractUnitTest {

    @Autowired
    private InventoryService inventoryService;

    @Autowired
    private ProductService productService;

    @Test
    public void addInventoryTest() throws ApiException {
        ProductPojo product = new ProductPojo();
        product.setBarcode("123ABC");
        product.setName("Test Product");
        productService.addProduct(product);

        InventoryPojo inventory = new InventoryPojo();
        inventory.setQuantity(10L);
        inventory.setBarcode("123ABC");
        inventory.setProductPojo(product);

        inventoryService.addInventory(inventory);

        InventoryPojo retrievedInventory = inventoryService.getInventoryByBarcode("123ABC");
        assertNotNull(retrievedInventory);
        assertEquals(10L, retrievedInventory.getQuantity().longValue());
        assertEquals("123ABC", retrievedInventory.getBarcode());
    }

    @Test
    public void getInventoryByIdTest() throws ApiException {
        ProductPojo product = new ProductPojo();
        product.setBarcode("456DEF");
        product.setName("Sample Product");
        productService.addProduct(product);

        InventoryPojo inventory = new InventoryPojo();
        inventory.setQuantity(20L);
        inventory.setBarcode("456DEF");
        inventory.setProductPojo(product);

        inventoryService.addInventory(inventory);
        InventoryPojo savedInventory = inventoryService.getInventoryByBarcode("456DEF");

        InventoryPojo retrievedInventory = inventoryService.getInventory(savedInventory.getId());

        assertNotNull(retrievedInventory);
        assertEquals("456DEF", retrievedInventory.getBarcode());
        assertEquals(20L, retrievedInventory.getQuantity().longValue());
    }

    @Test(expected = ApiException.class)
    public void getInventoryByIdNotFoundTest() throws ApiException {
        inventoryService.getInventory(999L);
    }

    @Test
    public void getInventoryByBarcodeTest() throws ApiException {
        ProductPojo product = new ProductPojo();
        product.setBarcode("789XYZ");
        product.setName("Another Product");
        productService.addProduct(product);

        InventoryPojo inventory = new InventoryPojo();
        inventory.setQuantity(15L);
        inventory.setBarcode("789XYZ");
        inventory.setProductPojo(product);

        inventoryService.addInventory(inventory);

        InventoryPojo retrievedInventory = inventoryService.getInventoryByBarcode("789XYZ");
        assertNotNull(retrievedInventory);
        assertEquals(15L, retrievedInventory.getQuantity().longValue());
    }

    @Test(expected = ApiException.class)
    public void getInventoryByBarcodeNotFoundTest() throws ApiException {
        inventoryService.getInventoryByBarcode("NONEXISTENT");
    }

    @Test
    public void getAllInventoriesTest() throws ApiException {
        ProductPojo product1 = new ProductPojo();
        product1.setBarcode("AAA111");
        product1.setName("First Product");
        productService.addProduct(product1);

        ProductPojo product2 = new ProductPojo();
        product2.setBarcode("BBB222");
        product2.setName("Second Product");
        productService.addProduct(product2);

        InventoryPojo inventory1 = new InventoryPojo();
        inventory1.setQuantity(30L);
        inventory1.setBarcode("AAA111");
        inventory1.setProductPojo(product1);

        InventoryPojo inventory2 = new InventoryPojo();
        inventory2.setQuantity(40L);
        inventory2.setBarcode("BBB222");
        inventory2.setProductPojo(product2);

        inventoryService.addInventory(inventory1);
        inventoryService.addInventory(inventory2);

        List<InventoryPojo> inventories = inventoryService.getAllInventories();
        assertEquals(2L, inventories.size());
    }

    @Test
    public void updateInventoryTest() throws ApiException {
        ProductPojo product = new ProductPojo();
        product.setBarcode("PQR789");
        product.setName("Some Product");
        productService.addProduct(product);

        InventoryPojo inventory = new InventoryPojo();
        inventory.setQuantity(25L);
        inventory.setBarcode("PQR789");
        inventory.setProductPojo(product);

        inventoryService.addInventory(inventory);
        InventoryPojo savedInventory = inventoryService.getInventoryByBarcode("PQR789");

        InventoryPojo updatedInventory = new InventoryPojo();
        updatedInventory.setQuantity(50L);
        updatedInventory.setBarcode("PQR789");
        updatedInventory.setProductPojo(product);

        inventoryService.updateInventory(savedInventory.getId(), updatedInventory);

        InventoryPojo retrievedInventory = inventoryService.getInventory(savedInventory.getId());
        assertEquals(50L, retrievedInventory.getQuantity().longValue());
    }

    @Test(expected = ApiException.class)
    public void updateInventoryNotFoundTest() throws ApiException {
        InventoryPojo updatedInventory = new InventoryPojo();
        updatedInventory.setQuantity(100L);
        updatedInventory.setBarcode("UNKNOWN");
        inventoryService.updateInventory(999L, updatedInventory);
    }

    @Test
    public void deleteInventoryTest() throws ApiException {
        ProductPojo product = new ProductPojo();
        product.setBarcode("LMN654");
        product.setName("To Delete Product");
        productService.addProduct(product);

        InventoryPojo inventory = new InventoryPojo();
        inventory.setQuantity(5L);
        inventory.setBarcode("LMN654");
        inventory.setProductPojo(product);

        inventoryService.addInventory(inventory);
        InventoryPojo savedInventory = inventoryService.getInventoryByBarcode("LMN654");

        inventoryService.deleteInventory(savedInventory.getId());

        try {
            inventoryService.getInventory(savedInventory.getId());
            fail("Expected ApiException, but no exception was thrown.");
        } catch (ApiException e) {
            assertEquals("Inventory with given ID does not exist, id: " + savedInventory.getId(), e.getMessage());
        }
    }

    @Test
    public void deleteInventoryNotFoundTest() {
        try {
            inventoryService.deleteInventory(999L);
        } catch (ApiException e) {
            assertEquals("Inventory with given ID does not exist, id: 999", e.getMessage());
        }
    }
}
