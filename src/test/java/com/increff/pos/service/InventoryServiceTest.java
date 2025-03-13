package com.increff.pos.service;

import static org.junit.Assert.*;

import com.increff.pos.db.pojo.InventoryPojo;
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
        inventory.setProdId(product.getId());
        inventoryService.addInventory(inventory);

        InventoryPojo retrievedInventory = inventoryService.getInventoryByBarcode("123ABC");
        assertNotNull(retrievedInventory);
        assertEquals(10L, retrievedInventory.getQuantity().longValue());
        assertEquals("123ABC", productService.getProduct(retrievedInventory.getProdId()).getBarcode());
    }

    @Test
    public void getInventoryByIdTest() throws ApiException {
        ProductPojo product = new ProductPojo();
        product.setBarcode("456DEF");
        product.setName("Sample Product");
        productService.addProduct(product);

        InventoryPojo inventory = new InventoryPojo();
        inventory.setQuantity(20L);
        inventory.setProdId(product.getId());

        inventoryService.addInventory(inventory);
        InventoryPojo savedInventory = inventoryService.getInventoryByBarcode("456DEF");

        InventoryPojo retrievedInventory = inventoryService.getCheck(savedInventory.getId());

        assertNotNull(retrievedInventory);
        assertEquals("456DEF", productService.getProduct(retrievedInventory.getProdId()).getBarcode());
        assertEquals(20L, retrievedInventory.getQuantity().longValue());
    }

    @Test(expected = ApiException.class)
    public void getInventoryByIdNotFoundTest() throws ApiException {
        inventoryService.getCheck(999L);
    }

    @Test
    public void getInventoryByBarcodeTest() throws ApiException {
        ProductPojo product = new ProductPojo();
        product.setBarcode("789XYZ");
        product.setName("Another Product");
        productService.addProduct(product);

        InventoryPojo inventory = new InventoryPojo();
        inventory.setQuantity(15L);
        inventory.setProdId(product.getId());
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
        inventory1.setProdId(product1.getId());

        InventoryPojo inventory2 = new InventoryPojo();
        inventory2.setQuantity(40L);
        inventory2.setProdId(product2.getId());
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
        inventory.setProdId(product.getId());
        inventoryService.addInventory(inventory);
        InventoryPojo savedInventory = inventoryService.getInventoryByBarcode("PQR789");

        InventoryPojo updatedInventory = new InventoryPojo();
        updatedInventory.setQuantity(50L);
        updatedInventory.setProdId(product.getId());

        inventoryService.updateInventory(savedInventory.getId(), updatedInventory);

        InventoryPojo retrievedInventory = inventoryService.getCheck(savedInventory.getId());
        assertEquals(50L, retrievedInventory.getQuantity().longValue());
    }

    @Test(expected = ApiException.class)
    public void updateInventoryNotFoundTest() throws ApiException {
        InventoryPojo updatedInventory = new InventoryPojo();
        updatedInventory.setQuantity(100L);
        inventoryService.updateInventory(999L, updatedInventory);
    }

}
