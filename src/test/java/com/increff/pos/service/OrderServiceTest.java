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

import java.time.ZonedDateTime;
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
    private OrderPojo order;

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

        order = new OrderPojo();
        order.setOrderDate(ZonedDateTime.now());
        orderService.createOrder(order);
    }

    @Test
    public void testCreateOrderAndOrderItem_Success() throws ApiException {
        OrderItemPojo orderItem = new OrderItemPojo();
        orderItem.setOrderId(order.getId());
        orderItem.setProdId(product.getId());
        orderItem.setQuantity(2L);
        orderItem.setSellingPrice(product.getPrice());

        InventoryPojo inventory = inventoryService.getInventoryByBarcode(product.getBarcode());
        orderService.validateOrderItemQuantity(orderItem, product, inventory);
        orderService.createOrderItem(orderItem);

        List<OrderItemPojo> items = orderService.getItemsByOrderId(order.getId());
        assertEquals(1, items.size());
        assertEquals(2L, items.get(0).getQuantity().longValue());

        OrderPojo savedOrder = orderDao.selectById(order.getId()).orElse(null);
        assertNotNull(savedOrder);
    }

    @Test(expected = ApiException.class)
    public void testValidateOrderItem_InsufficientStock() throws ApiException {
        OrderItemPojo orderItem = new OrderItemPojo();
        orderItem.setOrderId(order.getId());
        orderItem.setProdId(product.getId());
        orderItem.setQuantity(200L);
        orderItem.setSellingPrice(product.getPrice());

        InventoryPojo inventory = inventoryService.getInventoryByBarcode(product.getBarcode());
        orderService.validateOrderItemQuantity(orderItem, product, inventory);
    }

    @Test(expected = ApiException.class)
    public void testValidateOrderItem_NonPositiveQuantity() throws ApiException {
        OrderItemPojo orderItem = new OrderItemPojo();
        orderItem.setOrderId(order.getId());
        orderItem.setProdId(product.getId());
        orderItem.setQuantity(-1L);
        orderItem.setSellingPrice(product.getPrice());

        InventoryPojo inventory = inventoryService.getInventoryByBarcode(product.getBarcode());
        orderService.validateOrderItemQuantity(orderItem, product, inventory);
    }

    @Test
    public void testGetAllOrders() throws ApiException {
        OrderItemPojo orderItem = new OrderItemPojo();
        orderItem.setOrderId(order.getId());
        orderItem.setProdId(product.getId());
        orderItem.setQuantity(2L);
        orderItem.setSellingPrice(product.getPrice());

        InventoryPojo inventory = inventoryService.getInventoryByBarcode(product.getBarcode());
        orderService.validateOrderItemQuantity(orderItem, product, inventory);
        orderService.createOrderItem(orderItem);

        List<OrderPojo> orders = orderService.getAllOrders();

        assertFalse(orders.isEmpty());
        assertEquals(1, orders.size());
    }

    @Test
    public void testGetOrderById_Success() throws ApiException {
        OrderPojo retrievedOrder = orderService.getOrderById(order.getId());
        assertNotNull(retrievedOrder);
        assertEquals(order.getId(), retrievedOrder.getId());
    }

    @Test(expected = ApiException.class)
    public void testGetOrderById_NotFound() throws ApiException {
        orderService.getOrderById(999L);
    }

    @Test
    public void testGetItemsByOrderId() throws ApiException {
        OrderItemPojo orderItem = new OrderItemPojo();
        orderItem.setOrderId(order.getId());
        orderItem.setProdId(product.getId());
        orderItem.setQuantity(1L);
        orderItem.setSellingPrice(product.getPrice());

        InventoryPojo inventory = inventoryService.getInventoryByBarcode(product.getBarcode());
        orderService.validateOrderItemQuantity(orderItem, product, inventory);
        orderService.createOrderItem(orderItem);

        List<OrderItemPojo> items = orderService.getItemsByOrderId(order.getId());
        assertEquals(1, items.size());
        assertEquals(1L, items.get(0).getQuantity().longValue());
    }
}