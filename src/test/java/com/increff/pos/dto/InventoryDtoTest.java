//package com.increff.pos.dto;
//
//import com.increff.pos.db.pojo.ClientPojo;
//import com.increff.pos.model.data.InventoryData;
//import com.increff.pos.model.forms.InventoryForm;
//import com.increff.pos.model.forms.ProductForm;
//import com.increff.pos.service.*;
//import org.junit.Before;
//import org.junit.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//
//import static org.junit.Assert.*;
//
//import java.util.List;
//
//public class InventoryDtoTest extends AbstractUnitTest {
//
//    @Autowired
//    private InventoryDto inventoryDto;
//
//    @Autowired
//    private ProductDto productDto;
//
//    @Autowired
//    private ProductService productService;
//
//    @Autowired
//    private ClientService clientService;
//
//    @Autowired
//    private InventoryService inventoryService;
//
//    private String barcode;
//
//    @Before
//    public void setUp() throws ApiException {
//        ClientPojo client = new ClientPojo();
//        client.setName("Test Client");
//        clientService.addClient(client);
//
//        ProductForm productForm = new ProductForm();
//        productForm.setName("Test Product");
//        productForm.setBarcode("123ABC");
//        productForm.setPrice(100.0);
//        productForm.setClientName("Test Client");
//
//        productDto.addProduct(productForm);
//
//        barcode = "123ABC";
//    }
//
//    @Test
//    public void testAddInventory() throws ApiException {
//        InventoryForm form = new InventoryForm();
//        form.setBarcode(barcode);
//        form.setQuantity(50L);
//
//        inventoryDto.addInventory(form);
//
//        List<InventoryData> inventoryList = inventoryDto.getAllInventories();
//        assertEquals(1, inventoryList.size());
//        assertEquals(50L, inventoryList.get(0).getQuantity().longValue());
//    }
//
//    @Test
//    public void testGetInventoryById() throws ApiException {
//        InventoryForm form = new InventoryForm();
//        form.setBarcode(barcode);
//        form.setQuantity(25L);
//        inventoryDto.addInventory(form);
//
//        List<InventoryData> inventories = inventoryDto.getAllInventories();
//        InventoryData inventory = inventoryDto.getInventory(inventories.get(0).getId());
//
//        assertNotNull(inventory);
//        assertEquals(barcode, inventory.getBarcode());
//        assertEquals(25L, inventory.getQuantity().longValue());
//    }
//
//    @Test
//    public void testUpdateInventory() throws ApiException {
//        InventoryForm form = new InventoryForm();
//        form.setBarcode(barcode);
//        form.setQuantity(30L);
//        inventoryDto.addInventory(form);
//
//        List<InventoryData> inventories = inventoryDto.getAllInventories();
//        Long inventoryId = inventories.get(0).getId();
//
//        InventoryForm updateForm = new InventoryForm();
//        updateForm.setBarcode(barcode);
//        updateForm.setQuantity(60L);
//        inventoryDto.updateInventory(updateForm, inventoryId);
//
//        InventoryData updatedInventory = inventoryDto.getInventory(inventoryId);
//        assertEquals(60L, updatedInventory.getQuantity().longValue());
//    }
//
////    @Test
////    public void testDeleteInventory() throws ApiException {
////        InventoryForm form = new InventoryForm();
////        form.setBarcode(barcode);
////        form.setQuantity(40L);
////        inventoryDto.addInventory(form);
////
////        List<InventoryData> inventories = inventoryDto.getAllInventories();
////        Long inventoryId = inventories.get(0).getId();
////
////        inventoryDto.deleteInventory(inventoryId);
////
////        List<InventoryData> updatedInventories = inventoryDto.getAllInventories();
////        assertEquals(0, updatedInventories.size());
////    }
//}
