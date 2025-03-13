package com.increff.pos.service;

import com.increff.pos.db.dao.OrderDao;
import com.increff.pos.db.dao.OrderItemDao;
import com.increff.pos.db.pojo.*;
import com.increff.pos.util.ApiException;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.transaction.annotation.Transactional;

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
        product.setClientId(client.getId());
        product.setName("Test Product");
        product.setBarcode("123456789");
        product.setPrice(50.0);
        productService.addProduct(product);

        InventoryPojo inventory = new InventoryPojo();
        inventory.setProdId(product.getId());
        inventory.setQuantity(100L);
        inventoryService.addInventory(inventory);
    }

    @Test
    public void testCreateOrder_Success() throws ApiException {
        OrderItemPojo orderItem = new OrderItemPojo();
        orderItem.setProdId(product.getId());
        orderItem.setQuantity(2L);
        orderItem.setSellingPrice(product.getPrice());

        CustomerPojo customerPojo = new CustomerPojo();
        customerPojo.setName("Customer Test");
        customerPojo.setPhone("1234567890");

        OrderPojo createdOrder = orderService.createOrder(Collections.singletonList(orderItem), customerPojo);

        assertNotNull(createdOrder);

        InventoryPojo updatedInventory = inventoryService.getInventoryByBarcode(product.getBarcode());
        assertEquals(98, updatedInventory.getQuantity().intValue());

        OrderPojo savedOrder = orderDao.selectById(createdOrder.getId()).orElse(null);
        assertNotNull(savedOrder);
    }

    @Test(expected = ApiException.class)
    public void testCreateOrderInsufficientStock() throws ApiException {
        OrderItemPojo orderItem = new OrderItemPojo();
        orderItem.setProdId(product.getId());
        orderItem.setQuantity(200L);
        orderItem.setSellingPrice(product.getPrice());

        CustomerPojo customerPojo = new CustomerPojo();
        customerPojo.setName("Customer Test");
        customerPojo.setPhone("1234567890");

        orderService.createOrder(Collections.singletonList(orderItem), customerPojo);
    }

    @Test(expected = ApiException.class)
    public void testCreateOrderNonPositiveQuantity() throws ApiException {
        OrderItemPojo orderItem = new OrderItemPojo();
        orderItem.setProdId(product.getId());
        orderItem.setQuantity(-1L);
        orderItem.setSellingPrice(product.getPrice());

        CustomerPojo customerPojo = new CustomerPojo();
        customerPojo.setName("Customer Test");
        customerPojo.setPhone("1234567890");

        orderService.createOrder(Collections.singletonList(orderItem), customerPojo);
    }

    @Test
    public void testGetAllOrders() throws ApiException {
        OrderItemPojo orderItem = new OrderItemPojo();
        orderItem.setProdId(product.getId());
        orderItem.setQuantity(2L);
        orderItem.setSellingPrice(product.getPrice());

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
        orderItem.setProdId(product.getId());
        orderItem.setQuantity(1L);
        orderItem.setSellingPrice(product.getPrice());

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