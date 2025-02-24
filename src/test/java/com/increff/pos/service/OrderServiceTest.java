package com.increff.pos.service;

import static org.junit.Assert.*;

import com.increff.pos.db.dao.OrderDao;
import com.increff.pos.db.pojo.OrderPojo;
import com.increff.pos.db.pojo.ProductPojo;
import com.increff.pos.model.forms.SalesForm;
import com.increff.pos.model.forms.SalesForm.SaleItem;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = QaConfig.class)
@Transactional
@Rollback
public class OrderServiceTest extends AbstractUnitTest {

    @Autowired
    private OrderService orderService;

    @Autowired
    private ProductService productService;

    @Autowired
    private OrderDao orderDao;

    @Test
    public void createOrderTest() throws ApiException {
        ProductPojo product = new ProductPojo();
        product.setBarcode("PROD123");
        product.setName("Test Product");
        product.setPrice(50.0);
        productService.addProduct(product);

        SaleItem saleItem = new SaleItem();
        saleItem.setBarcode("PROD123");
        saleItem.setQuantity(2);
        saleItem.setSaleDate("10-Feb-2020");

        SalesForm salesForm = new SalesForm();
        salesForm.setItems(Collections.singletonList(saleItem));

        OrderPojo order = orderService.createOrder(salesForm);
        assertNotNull(order);
        assertEquals(100.0, order.getTotalAmount(), 0.01);
    }

    @Test(expected = ApiException.class)
    public void createOrderWithInvalidProductTest() throws ApiException {
        SaleItem saleItem = new SaleItem();
        saleItem.setBarcode("INVALID123");
        saleItem.setQuantity(1);
        saleItem.setSaleDate("10-Feb-2020");

        SalesForm salesForm = new SalesForm();
        salesForm.setItems(Collections.singletonList(saleItem));

        orderService.createOrder(salesForm);
    }

    @Test
    public void getAllOrdersTest() throws ApiException {
        List<OrderPojo> orders = orderService.getAllOrders();
        assertNotNull(orders);
    }

    @Test(expected = ApiException.class)
    public void getOrderByIdNotFoundTest() throws ApiException {
        orderService.getOrderById(999L);
    }

    @Test
    public void updateOrderTest() throws ApiException {
        ProductPojo product = new ProductPojo();
        product.setBarcode("PROD456");
        product.setName("Updated Product");
        product.setPrice(40.0);
        productService.addProduct(product);

        SaleItem saleItem = new SaleItem();
        saleItem.setBarcode("PROD456");
        saleItem.setQuantity(3);
        saleItem.setSaleDate("10-Feb-2020");

        SalesForm salesForm = new SalesForm();
        salesForm.setItems(Collections.singletonList(saleItem));

        OrderPojo order = orderService.createOrder(salesForm);

        SaleItem updatedSaleItem = new SaleItem();
        updatedSaleItem.setBarcode("PROD456");
        updatedSaleItem.setQuantity(5);
        updatedSaleItem.setSaleDate("10-Feb-2020");

        SalesForm updatedSalesForm = new SalesForm();
        updatedSalesForm.setItems(Collections.singletonList(updatedSaleItem));

        orderService.updateOrder(order.getId(), updatedSalesForm);
        OrderPojo updatedOrder = orderService.getOrderById(order.getId());
        assertEquals(200.0, updatedOrder.getTotalAmount(), 0.01);
    }

//    @Test(expected = ApiException.class)
//    public void deleteOrderWithInvalidIdTest() throws ApiException {
//        Long invalidOrderId = 999L;
//        orderService.deleteOrder(invalidOrderId);
//    }

//    @Test
//    public void deleteOrderTest() throws ApiException {
//        OrderPojo order = new OrderPojo();
//        order.setTotalAmount(100.0);
//        order.setOrderDate("10-Feb-2020");
//        orderDao.add(order);
//        assertNotNull(orderDao.selectById(order.getId()));
//        orderService.deleteOrder(order.getId());
//        assertFalse(orderDao.selectById(order.getId()).isPresent());
//    }

}