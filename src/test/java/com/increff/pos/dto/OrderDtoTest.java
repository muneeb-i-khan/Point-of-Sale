package com.increff.pos.dto;

import com.increff.pos.db.dao.ClientDao;
import com.increff.pos.db.dao.InventoryDao;
import com.increff.pos.db.dao.ProductDao;
import com.increff.pos.db.pojo.ClientPojo;
import com.increff.pos.db.pojo.InventoryPojo;
import com.increff.pos.db.pojo.ProductPojo;
import com.increff.pos.flow.OrderFlow;
import com.increff.pos.model.data.OrderData;
import com.increff.pos.model.forms.CustomerForm;
import com.increff.pos.model.forms.OrderForm.OrderItemForm;
import com.increff.pos.service.AbstractUnitTest;
import com.increff.pos.service.OrderService;
import com.increff.pos.service.QaConfig;
import com.increff.pos.util.ApiException;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.*;

@ContextConfiguration(classes = QaConfig.class)
@Transactional
@Rollback
public class OrderDtoTest extends AbstractUnitTest {

    @Autowired
    private OrderDto orderDto;

    @Autowired
    private OrderFlow orderFlow;

    @Autowired
    private OrderService orderService;

    @Autowired
    private ClientDao clientDao;

    @Autowired
    private ProductDao productDao;

    @Autowired
    private InventoryDao inventoryDao;

    private ProductPojo product1;
    private ProductPojo product2;
    private ClientPojo client;

    @Before
    public void setUp() {
        client = new ClientPojo();
        client.setName("Test Client");
        client.setDescription("A sample test client");
        clientDao.add(client);

        product1 = new ProductPojo();
        product1.setClientId(client.getId());
        product1.setBarcode("123456789");
        product1.setName("Product A");
        product1.setPrice(100.0);
        productDao.add(product1);

        product2 = new ProductPojo();
        product2.setClientId(client.getId());
        product2.setBarcode("987654321");
        product2.setName("Product B");
        product2.setPrice(150.0);
        productDao.add(product2);

        InventoryPojo inventory1 = new InventoryPojo();
        inventory1.setProdId(product1.getId());
        inventory1.setQuantity(10L);
        inventoryDao.add(inventory1);

        InventoryPojo inventory2 = new InventoryPojo();
        inventory2.setProdId(product2.getId());
        inventory2.setQuantity(15L);
        inventoryDao.add(inventory2);
    }

    @Test
    public void testAddOrderSuccess() throws ApiException {
        OrderItemForm orderItem1 = new OrderItemForm();
        orderItem1.setBarcode(product1.getBarcode());
        orderItem1.setQuantity(2L);
        orderItem1.setSellingPrice(product1.getPrice());

        OrderItemForm orderItem2 = new OrderItemForm();
        orderItem2.setBarcode(product2.getBarcode());
        orderItem2.setQuantity(3L);
        orderItem2.setSellingPrice(product2.getPrice());

        CustomerForm customer = new CustomerForm();
        customer.setName("Customer Test");
        customer.setPhone("1234567890");

        List<OrderItemForm> orderItems = Arrays.asList(orderItem1, orderItem2);
        OrderData orderData = orderDto.addOrder(orderItems, customer);

        assertNotNull(orderData);
        assertEquals(2, orderData.getItems().size());
        assertEquals("Customer Test", orderData.getCustomerName());
        assertEquals("1234567890", orderData.getCustomerPhone());
        // Verify order items
        assertEquals(product1.getBarcode(), orderData.getItems().get(0).getBarcode());
        assertEquals(2, orderData.getItems().get(0).getQuantity());
        assertEquals(product2.getBarcode(), orderData.getItems().get(1).getBarcode());
        assertEquals(3, orderData.getItems().get(1).getQuantity());
    }

    @Test(expected = ApiException.class)
    public void testAddOrderEmptyList() throws ApiException {
        CustomerForm customer = new CustomerForm();
        customer.setName("Customer Test");
        customer.setPhone("1234567890");
        orderDto.addOrder(Arrays.asList(), customer);
    }

    @Test(expected = ApiException.class)
    public void testAddOrderInvalidBarcode() throws ApiException {
        OrderItemForm orderItem = new OrderItemForm();
        orderItem.setBarcode("INVALID_BARCODE");
        orderItem.setQuantity(1L);
        orderItem.setSellingPrice(100.0);

        CustomerForm customer = new CustomerForm();
        customer.setName("Customer Test");
        customer.setPhone("1234567890");

        orderDto.addOrder(Arrays.asList(orderItem), customer);
    }

    @Test
    public void testGetAllOrders() throws ApiException {
        testAddOrderSuccess();
        List<OrderData> orders = orderDto.getAllOrders();

        assertNotNull(orders);
        assertEquals(1, orders.size());
        assertEquals(2, orders.get(0).getItems().size());
        assertEquals("Customer Test", orders.get(0).getCustomerName());
    }

    @Test(expected = ApiException.class)
    public void testGetOrderNotFound() throws ApiException {
        orderDto.getOrder(999L);
    }

    @Test
    public void testGetOrderSuccess() throws ApiException {
        OrderData createdOrder = testAddOrderSuccessReturnData();
        OrderData retrievedOrder = orderDto.getOrder(createdOrder.getId());

        assertNotNull(retrievedOrder);
        assertEquals(createdOrder.getId(), retrievedOrder.getId());
        assertEquals(createdOrder.getCustomerName(), retrievedOrder.getCustomerName());
        assertEquals(createdOrder.getCustomerPhone(), retrievedOrder.getCustomerPhone());
        assertEquals(createdOrder.getItems().size(), retrievedOrder.getItems().size());
    }


    private OrderData testAddOrderSuccessReturnData() throws ApiException {
        OrderItemForm orderItem1 = new OrderItemForm();
        orderItem1.setBarcode(product1.getBarcode());
        orderItem1.setQuantity(2L);
        orderItem1.setSellingPrice(product1.getPrice());

        OrderItemForm orderItem2 = new OrderItemForm();
        orderItem2.setBarcode(product2.getBarcode());
        orderItem2.setQuantity(3L);
        orderItem2.setSellingPrice(product2.getPrice());

        CustomerForm customer = new CustomerForm();
        customer.setName("Customer Test");
        customer.setPhone("1234567890");

        List<OrderItemForm> orderItems = Arrays.asList(orderItem1, orderItem2);
        OrderData orderData = orderDto.addOrder(orderItems, customer);
        assertNotNull(String.valueOf(orderData), "Order data should not be null after creation");
        return orderData;
    }
}