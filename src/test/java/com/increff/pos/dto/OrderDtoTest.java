//package com.increff.pos.dto;
//
//import com.increff.pos.db.dao.ClientDao;
//import com.increff.pos.db.dao.InventoryDao;
//import com.increff.pos.db.dao.ProductDao;
//import com.increff.pos.db.pojo.ClientPojo;
//import com.increff.pos.db.pojo.InventoryPojo;
//import com.increff.pos.db.pojo.ProductPojo;
//import com.increff.pos.flow.OrderFlow;
//import com.increff.pos.model.data.OrderData;
//import com.increff.pos.model.forms.CustomerForm;
//import com.increff.pos.model.forms.OrderItemForm;
//import com.increff.pos.service.AbstractUnitTest;
//import com.increff.pos.util.ApiException;
//import com.increff.pos.service.QaConfig;
//import org.junit.Before;
//import org.junit.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.test.annotation.Rollback;
//import org.springframework.test.context.ContextConfiguration;
//import org.springframework.transaction.annotation.Transactional;
//
//import java.util.Arrays;
//import java.util.Collections;
//import java.util.List;
//
//import static org.junit.Assert.*;
//
//@ContextConfiguration(classes = QaConfig.class)
//@Transactional
//@Rollback
//public class OrderDtoTest extends AbstractUnitTest {
//
//    @Autowired
//    private OrderDto orderDto;
//
//    @Autowired
//    private OrderFlow orderFlow;
//
//    @Autowired
//    private ClientDao clientDao;
//
//    @Autowired
//    private ProductDao productDao;
//
//    @Autowired
//    private InventoryDao inventoryDao;
//
//    private ProductPojo product1;
//    private ProductPojo product2;
//
//    @Before
//    public void setUp() {
//        ClientPojo client = new ClientPojo();
//        client.setName("Test Client");
//        client.setDescription("A sample test client");
//        clientDao.add(client);
//
//        product1 = new ProductPojo();
//        product1.setClient_id(client.getId());
//        product1.setBarcode("123456789");
//        product1.setName("Product A");
//        product1.setPrice(100.0);
//        productDao.add(product1);
//
//        product2 = new ProductPojo();
//        product2.setClient_id(client.getId());
//        product2.setBarcode("987654321");
//        product2.setName("Product B");
//        product2.setPrice(150.0);
//        productDao.add(product2);
//
//        InventoryPojo inventory1 = new InventoryPojo();
//        inventory1.setProd_id(product1.getId());
//        inventory1.setQuantity(10L);
//        inventoryDao.add(inventory1);
//
//        InventoryPojo inventory2 = new InventoryPojo();
//        inventory2.setProd_id(product2.getId());
//        inventory2.setQuantity(15L);
//        inventoryDao.add(inventory2);
//    }
//
//    @Test
//    public void testAddOrderSuccess() throws ApiException {
//        OrderItemForm orderItem1 = new OrderItemForm();
//        orderItem1.setBarcode(product1.getBarcode());
//        orderItem1.setQuantity(2L);
//
//        OrderItemForm orderItem2 = new OrderItemForm();
//        orderItem2.setBarcode(product2.getBarcode());
//        orderItem2.setQuantity(3L);
//
//        CustomerForm customer = new CustomerForm();
//        customer.setName("Customer Test");
//        customer.setPhone("123456789");
//
//        List<OrderItemForm> orderItems = Arrays.asList(orderItem1, orderItem2);
//
//        orderDto.addOrder(orderItems,customer);
//        List<OrderData> orders = orderDto.getAllOrders();
//
//        assertNotNull(orders);
//        assertEquals(1, orders.size());
//    }
//
//    @Test(expected = ApiException.class)
//    public void testAddOrderEmptyList() throws ApiException {
//        CustomerForm customer = new CustomerForm();
//        customer.setName("Customer Test");
//        customer.setPhone("123456789");
//
//        orderDto.addOrder(Collections.emptyList(),customer);
//    }
//
//
//
//    @Test
//    public void testGetAllOrders() throws ApiException {
//        testAddOrderSuccess();
//
//        List<OrderData> orders = orderDto.getAllOrders();
//
//        assertNotNull(orders);
//        assertFalse(orders.isEmpty());
//        assertEquals(1, orders.size());
//    }
//
//    @Test(expected = ApiException.class)
//    public void testGetOrderNotFound() throws ApiException {
//        orderDto.getOrder(999L);
//    }
//
//    @Test
//    public void testGetOrderSuccess() throws ApiException {
//        testAddOrderSuccess();
//        OrderData savedOrder = orderDto.getAllOrders().get(0);
//        OrderData order = orderDto.getOrder(savedOrder.getId());
//        assertNotNull(order);
//        assertEquals(savedOrder.getId(), order.getId());
//    }
//}
