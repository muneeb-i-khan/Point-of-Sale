package com.increff.pos.flow;

import com.increff.pos.db.pojo.*;
import com.increff.pos.model.data.OrderData;
import com.increff.pos.model.data.OrderData.OrderItem;
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

    public OrderData addOrder(List<OrderItemForm> orderItemFormList, CustomerPojo customerPojo) throws ApiException {
        if (orderItemFormList == null || orderItemFormList.isEmpty()) {
            throw new ApiException("Order cannot be empty.");
        }
        List<OrderItemForm> mergedOrderItems = mergeDuplicateItems(orderItemFormList);
        List<OrderItemPojo> orderItemPojoList = convert(mergedOrderItems);
        OrderPojo orderPojo = createOrder(orderItemPojoList, customerPojo);
        return convert(orderPojo);
    }

    public OrderData getOrder(Long id) throws ApiException {
        OrderPojo orderPojo = orderService.getOrderById(id);
        return convert(orderPojo);
    }

    public List<OrderData> getAllOrders() throws ApiException {
        List<OrderPojo> orderPojos = orderService.getAllOrders();
        List<OrderData> orderDataList = new ArrayList<>();

        for (OrderPojo order : orderPojos) {
            orderDataList.add(convert(order));
        }
        return orderDataList;
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

    // TODO: move to dto
    public OrderData convert(OrderPojo orderPojo) throws ApiException {
        if (orderPojo == null) {
            throw new ApiException("Order cannot be null");
        }

        OrderData orderData = new OrderData();
        orderData.setId(orderPojo.getId());
        orderData.setOrderDate(orderPojo.getOrderDate());

        CustomerPojo customer = customerService.getCheck(orderPojo.getId());
        orderData.setCustomerName(customer != null ? customer.getName() : null);
        orderData.setCustomerPhone(customer != null ? customer.getPhone() : null);

        List<OrderItemPojo> orderItemPojos = orderService.getItemsByOrderId(orderPojo.getId());
        if (orderItemPojos == null) {
            throw new ApiException("No items found for order ID: " + orderPojo.getId());
        }

        List<OrderItem> orderItems = new ArrayList<>();
        double totalAmount = 0.0;

        for (OrderItemPojo itemPojo : orderItemPojos) {
            OrderItem orderItem = new OrderItem();
            ProductPojo product = productService.getCheck(itemPojo.getProdId());
            if (product == null) {
                throw new ApiException("Product not found for ID: " + itemPojo.getProdId() + " in order: " + orderPojo.getId());
            }
            orderItem.setBarcode(product.getBarcode());
            orderItem.setQuantity(itemPojo.getQuantity().intValue());
            orderItem.setProdName(product.getName());
            orderItem.setPrice(product.getPrice());
            orderItem.setSellingPrice(itemPojo.getSellingPrice());
            // TODO: order details
            totalAmount += itemPojo.getSellingPrice() * itemPojo.getQuantity();

            orderItems.add(orderItem);
        }

        orderData.setItems(orderItems);
        orderData.setTotalAmount(totalAmount);
        return orderData;
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

    private void updateInventoryForOrderItem(OrderItemPojo orderItem) throws ApiException {
        ProductPojo productPojo = getProduct(orderItem.getProdId());
        InventoryPojo inventoryPojo = getInventoryByBarcode(productPojo.getBarcode());

        orderService.validateOrderItemQuantity(orderItem, productPojo, inventoryPojo);
        inventoryPojo.setQuantity(inventoryPojo.getQuantity() - orderItem.getQuantity());
    }

    private void processOrderItems(OrderPojo order, List<OrderItemPojo> orderItemPojoList) throws ApiException {
        for (OrderItemPojo orderItem : orderItemPojoList) {
            orderItem.setOrderId(order.getId());
            orderService.createOrderItem(orderItem);
            updateInventoryForOrderItem(orderItem);
        }
    }

    private List<OrderItemPojo> convert(List<OrderItemForm> orderItemFormList) throws ApiException {
        List<OrderItemPojo> orderItemPojoList = new ArrayList<>();

        for (OrderItemForm form : orderItemFormList) {
            ProductPojo product = productService.getProductByBarcode(form.getBarcode());

            if (product == null) {
                throw new ApiException("Product with barcode " + form.getBarcode() + " not found");
            }

            OrderItemPojo orderItemPojo = new OrderItemPojo();
            orderItemPojo.setProdId(product.getId());
            orderItemPojo.setQuantity(form.getQuantity());
            if(form.getSellingPrice() <= product.getPrice()) {
                orderItemPojo.setSellingPrice(form.getSellingPrice());
            }
            else {
                throw new ApiException("Selling price can't be more than Product's MRP");
            }
            orderItemPojoList.add(orderItemPojo);
        }

        return orderItemPojoList;
    }

    private List<OrderItemForm> mergeDuplicateItems(List<OrderItemForm> orderItemForms) {
        Map<String, OrderItemForm> mergedItems = new HashMap<>();

        for (OrderItemForm item : orderItemForms) {
            String barcode = item.getBarcode();
            if (mergedItems.containsKey(barcode)) {
                OrderItemForm existingItem = mergedItems.get(barcode);
                existingItem.setQuantity(existingItem.getQuantity() + item.getQuantity());
            } else {
                mergedItems.put(barcode, item);
            }
        }

        return new ArrayList<>(mergedItems.values());
    }
}
