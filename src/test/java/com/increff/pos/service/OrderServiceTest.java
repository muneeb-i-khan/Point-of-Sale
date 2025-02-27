package com.increff.pos.service;

import com.increff.pos.db.dao.OrderDao;
import com.increff.pos.db.dao.OrderItemDao;
import com.increff.pos.db.pojo.*;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.*;

@ContextConfiguration(classes = QaConfig.class)
@Transactional
@Rollback
public class OrderServiceTest extends AbstractUnitTest {

    @Autowired
    private OrderService orderService;

    @Autowired
    private OrderDao orderDao;

    @Autowired
    private OrderItemDao orderItemDao;

    @Autowired
    private ProductService productService;

    @Autowired
    private InventoryService inventoryService;

    @Autowired
    private ClientService clientService;

    private ProductPojo product;

    @Before
    public void setUp() throws ApiException {
        ClientPojo client = new ClientPojo();
        client.setName("Test Client");
        client.setDescription("A test client for orders");
        clientService.addClient(client);

        product = new ProductPojo();
        product.setClient_id(client.getId());
        product.setName("Test Product");
        product.setBarcode("123456789");
        product.setPrice(50.0);
        productService.addProduct(product);

        InventoryPojo inventory = new InventoryPojo();
        inventory.setProd_id(product.getId());
        inventory.setQuantity(100L);
        inventoryService.addInventory(inventory);
    }

    @Test
    public void testCreateOrder_Success() throws ApiException {
        OrderItemPojo orderItem = new OrderItemPojo();
        orderItem.setProd_id(product.getId());
        orderItem.setQuantity(2L);

        CustomerPojo customerPojo = new CustomerPojo();
        customerPojo.setName("Customer Test");
        customerPojo.setPhone("1234567890");

        OrderPojo createdOrder = orderService.createOrder(Collections.singletonList(orderItem),customerPojo);

        assertNotNull(createdOrder);
        assertEquals(LocalDate.now(), createdOrder.getOrderDate());
        assertEquals(100.0, createdOrder.getTotalAmount(), 0.01);

        InventoryPojo updatedInventory = inventoryService.getInventoryByBarcode(product.getBarcode());
        assertEquals(98, updatedInventory.getQuantity().intValue());

        OrderPojo savedOrder = orderDao.selectById(createdOrder.getId()).orElse(null);
        assertNotNull(savedOrder);
        assertEquals(100.0, savedOrder.getTotalAmount(), 0.01);
    }

    @Test(expected = ApiException.class)
    public void testCreateOrderInsufficientStock() throws ApiException {
        OrderItemPojo orderItem = new OrderItemPojo();
        orderItem.setProd_id(product.getId());
        orderItem.setQuantity(200L);

        CustomerPojo customerPojo = new CustomerPojo();
        customerPojo.setName("Customer Test");
        customerPojo.setPhone("1234567890");

        orderService.createOrder(Collections.singletonList(orderItem), customerPojo);
    }

    @Test(expected = ApiException.class)
    public void testCreateOrderNonPositiveQuantity() throws ApiException {
        OrderItemPojo orderItem = new OrderItemPojo();
        orderItem.setProd_id(product.getId());
        orderItem.setQuantity(-1L);

        CustomerPojo customerPojo = new CustomerPojo();
        customerPojo.setName("Customer Test");
        customerPojo.setPhone("1234567890");

        orderService.createOrder(Collections.singletonList(orderItem), customerPojo);
    }



    @Test
    public void testGetAllOrders() throws ApiException {
        OrderItemPojo orderItem = new OrderItemPojo();
        orderItem.setProd_id(product.getId());
        orderItem.setQuantity(2L);

        CustomerPojo customerPojo = new CustomerPojo();
        customerPojo.setName("Customer Test");
        customerPojo.setPhone("1234567890");

        orderService.createOrder(Collections.singletonList(orderItem), customerPojo);

        List<OrderPojo> orders = orderService.getAllOrders();

        assertFalse(orders.isEmpty());
        assertEquals(1, orders.size());
    }

    @Test
    public void testGetOrderById_Success() throws ApiException {
        OrderItemPojo orderItem = new OrderItemPojo();
        orderItem.setProd_id(product.getId());
        orderItem.setQuantity(1L);

        CustomerPojo customerPojo = new CustomerPojo();
        customerPojo.setName("Customer Test");
        customerPojo.setPhone("1234567890");

        OrderPojo createdOrder = orderService.createOrder(Collections.singletonList(orderItem), customerPojo);

        OrderPojo retrievedOrder = orderService.getOrderById(createdOrder.getId());

        assertNotNull(retrievedOrder);
        assertEquals(createdOrder.getId(), retrievedOrder.getId());
    }

    @Test(expected = ApiException.class)
    public void testGetOrderById_NotFound() throws ApiException {
        orderService.getOrderById(999L);
    }
}
