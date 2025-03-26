package com.increff.pos.flow;

import com.increff.pos.db.pojo.*;
import com.increff.pos.model.forms.OrderForm.OrderItemForm;
import com.increff.pos.service.*;
import com.increff.pos.util.ApiException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.ZonedDateTime;
import java.util.*;

@Component
public class OrderFlow {

    @Autowired
    private OrderService orderService;

    @Autowired
    private ProductService productService;

    @Autowired
    private CustomerService customerService;

    @Autowired
    private InventoryService inventoryService;

    @Autowired
    private SalesReportService salesReportService;

    public OrderPojo addOrder(List<OrderItemPojo> orderItemPojoList, CustomerPojo customerPojo) throws ApiException {
        if (orderItemPojoList == null || orderItemPojoList.isEmpty()) {
            throw new ApiException("Order cannot be empty.");
        }
        return createOrder(orderItemPojoList, customerPojo);
    }

    public OrderPojo getOrder(Long id) throws ApiException {
        return orderService.getOrderById(id);
    }

    public List<OrderPojo> getAllOrders() throws ApiException {
        List<OrderPojo> orderPojos = orderService.getAllOrders();
        List<OrderPojo> orderPojoList = new ArrayList<>();

        for (OrderPojo order : orderPojos) {
            orderPojoList.add(order);
        }
        return orderPojoList;
    }

    public void addCustomer(CustomerPojo customerPojo) {
        customerService.addCustomer(customerPojo);
    }

    public InventoryPojo getInventoryByBarcode(String barcode) {
        return inventoryService.getInventoryByBarcode(barcode);
    }

    public ProductPojo getProduct(Long id) {
        return productService.getCheck(id);
    }

    public OrderPojo initializeOrder(CustomerPojo customerPojo) throws ApiException {
        OrderPojo order = new OrderPojo();
        order.setOrderDate(ZonedDateTime.now());
        order.setInvoicePath("");
        addCustomer(customerPojo);
        order.setCustomerId(customerPojo.getId());

        return order;
    }

    public OrderPojo createOrder(List<OrderItemPojo> orderItemPojoList, CustomerPojo customerPojo) throws ApiException {
        OrderPojo order = initializeOrder(customerPojo);
        orderService.createOrder(order);
        processOrderItems(order, orderItemPojoList);

        double totalAmt = calculateTotalAmount(orderItemPojoList);

        updateSalesReport(order, orderItemPojoList, totalAmt);

        return order;
    }


    private void updateSalesReport(OrderPojo order, List<OrderItemPojo> orderItems, double totalAmount) throws ApiException {
        Long clientId = getProduct(orderItems.get(0).getProdId()).getClientId();
        SalesReportPojo report = salesReportService.findByClientAndDate(clientId, order.getOrderDate());
        long totalItemsSold = orderItems.stream().mapToLong(OrderItemPojo::getQuantity).sum();

        if (report == null) {
            createNewSalesReport(clientId, order.getOrderDate(), totalItemsSold, totalAmount);
        } else {
            updateExistingSalesReport(report, totalItemsSold, totalAmount);
        }
    }

    private void createNewSalesReport(Long clientId, ZonedDateTime date, long totalItemsSold, double totalAmount) throws ApiException {
        SalesReportPojo report = new SalesReportPojo();

        report.setClientId(clientId);
        report.setDate(date);
        report.setItemSold(totalItemsSold);
        report.setRevenue((long) totalAmount);

        salesReportService.add(report);
    }

    private void updateExistingSalesReport(SalesReportPojo report, long totalItemsSold, double totalAmount) throws ApiException {

        report.setItemSold(report.getItemSold() + totalItemsSold);
        report.setRevenue(report.getRevenue() + (long) totalAmount);

        salesReportService.update(report);
    }

    private double calculateTotalAmount(List<OrderItemPojo> orderItemPojoList) {
        return orderItemPojoList.stream()
                .mapToDouble(item -> item.getSellingPrice() * item.getQuantity())
                .sum();
    }

    private void validateOrderItemQuantity(OrderItemPojo orderItem, ProductPojo productPojo, InventoryPojo inventoryPojo) throws ApiException {
        if (orderItem.getQuantity() <= 0) {
            throw new ApiException("Quantity can't be negative");
        }
        if (inventoryPojo.getQuantity() < orderItem.getQuantity()) {
            throw new ApiException("Insufficient stock for product: " + productPojo.getName());
        }
    }
    private void updateInventoryForOrderItem(OrderItemPojo orderItem) throws ApiException {
        ProductPojo productPojo = getProduct(orderItem.getProdId());
        InventoryPojo inventoryPojo = getInventoryByBarcode(productPojo.getBarcode());

        validateOrderItemQuantity(orderItem, productPojo, inventoryPojo);
        inventoryService.setQuantity(inventoryPojo,inventoryPojo.getQuantity() - orderItem.getQuantity());
    }

    private void processOrderItems(OrderPojo order, List<OrderItemPojo> orderItemPojoList) throws ApiException {
        for (OrderItemPojo orderItem : orderItemPojoList) {
            orderItem.setOrderId(order.getId());
            orderService.createOrderItem(orderItem);
            updateInventoryForOrderItem(orderItem);
        }
    }
}
