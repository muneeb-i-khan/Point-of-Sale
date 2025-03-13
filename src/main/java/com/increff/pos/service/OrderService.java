package com.increff.pos.service;

import com.increff.pos.db.dao.OrderDao;
import com.increff.pos.db.dao.OrderItemDao;
import com.increff.pos.db.pojo.*;
import com.increff.pos.flow.OrderFlow;
import com.increff.pos.util.ApiException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import javax.transaction.Transactional;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.ZonedDateTime;
import java.util.Base64;
import java.util.List;

@Service
@Transactional(rollbackOn = ApiException.class)
public class OrderService {

    @Autowired
    private OrderItemDao orderItemDao;

    @Autowired
    private OrderDao orderDao;

    @Autowired
    private SalesReportService salesReportService;

    @Autowired
    private OrderFlow orderFlow;

    public OrderPojo createOrder(List<OrderItemPojo> orderItemPojoList, CustomerPojo customerPojo) throws ApiException {
        OrderPojo order = initializeOrder(customerPojo);
        orderDao.add(order);
        processOrderItems(order, orderItemPojoList);
        double totalAmt = calculateTotalAmount(orderItemPojoList);
        updateSalesReport(order, orderItemPojoList, totalAmt);
        return order;
    }

    public List<OrderPojo> getAllOrders() {
        return orderDao.selectAll();
    }

    public OrderPojo getOrderById(Long id) throws ApiException {
        return orderDao.selectById(id)
                .orElseThrow(() -> new ApiException("Order with ID " + id + " not found"));
    }

    public int countOrdersByDate(ZonedDateTime date) {
        return orderDao.countOrdersByDate(date);
    }

    public int countItemsSoldByDate(ZonedDateTime date) {
        return orderDao.countItemsSoldByDate(date);
    }

    public Double calculateRevenueByDate(ZonedDateTime date) {
        return orderDao.calculateRevenueByDate(date);
    }

    public List<OrderPojo> getAllOrdersPaginated(int page, int pageSize) {
        return orderDao.selectAllPaginated(page, pageSize);
    }

    public Long getOrderCount() {
        return orderDao.countOrders();
    }

    public ResponseEntity<byte[]> downloadPdf(Long id) throws ApiException {
        OrderPojo orderPojo = getOrderById(id);
        ResponseEntity<byte[]> existingInvoiceResponse = tryLoadExistingInvoice(orderPojo, id);
        if (existingInvoiceResponse != null) {
            return existingInvoiceResponse;
        }
        return generateAndSaveNewInvoice(orderPojo, id);
    }

    public List<OrderItemPojo> getItemsByOrderId(Long id) {
        return orderItemDao.getItemsByOrderId(id);
    }

    private OrderPojo initializeOrder(CustomerPojo customerPojo) throws ApiException {
        OrderPojo order = new OrderPojo();
        order.setOrderDate(ZonedDateTime.now());
        order.setInvoicePath("");
        orderFlow.addCustomer(customerPojo);
        order.setCustomerId(customerPojo.getId());
        return order;
    }

    private void processOrderItems(OrderPojo order, List<OrderItemPojo> orderItemPojoList) throws ApiException {
        for (OrderItemPojo orderItem : orderItemPojoList) {
            orderItem.setOrderId(order.getId());
            orderItemDao.add(orderItem);
            updateInventoryForOrderItem(orderItem);
        }
    }

    private void updateInventoryForOrderItem(OrderItemPojo orderItem) throws ApiException {
        ProductPojo productPojo = orderFlow.getProduct(orderItem.getProdId());
        InventoryPojo inventoryPojo = orderFlow.getInventoryByBarcode(productPojo.getBarcode());

        validateOrderItemQuantity(orderItem, productPojo, inventoryPojo);
        inventoryPojo.setQuantity(inventoryPojo.getQuantity() - orderItem.getQuantity());
    }

    private void validateOrderItemQuantity(OrderItemPojo orderItem, ProductPojo productPojo, InventoryPojo inventoryPojo) throws ApiException {
        if (orderItem.getQuantity() <= 0) {
            throw new ApiException("Quantity can't be negative");
        }
        if (inventoryPojo.getQuantity() < orderItem.getQuantity()) {
            throw new ApiException("Insufficient stock for product: " + productPojo.getName());
        }
    }

    private double calculateTotalAmount(List<OrderItemPojo> orderItemPojoList) {
        return orderItemPojoList.stream()
                .mapToDouble(item -> item.getSellingPrice() * item.getQuantity())
                .sum();
    }

    private void updateSalesReport(OrderPojo order, List<OrderItemPojo> orderItems, double totalAmount) throws ApiException {
        Long clientId = orderFlow.getProduct(orderItems.get(0).getProdId()).getClientId();
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


    private ResponseEntity<byte[]> tryLoadExistingInvoice(OrderPojo orderPojo, Long id) throws ApiException {
        if (orderPojo.getInvoicePath() != null && !orderPojo.getInvoicePath().isEmpty()) {
            File pdfFile = new File(orderPojo.getInvoicePath());
            if (pdfFile.exists()) {
                try {
                    byte[] pdfBytes = Files.readAllBytes(pdfFile.toPath());
                    return buildPdfResponse(pdfBytes, id);
                } catch (IOException e) {
                    throw new ApiException("Failed to read existing invoice file for order ID: " + id);
                }
            }
        }
        return null;
    }

    private ResponseEntity<byte[]> generateAndSaveNewInvoice(OrderPojo orderPojo, Long id) throws ApiException {
        String url = "http://localhost:9001/invoice/api/invoice/" + id;
        RestTemplate restTemplate = new RestTemplate();
        try {
            byte[] pdfBytes = fetchAndDecodeInvoice(url, restTemplate);
            saveInvoiceFile(pdfBytes, id, orderPojo);
            return buildPdfResponse(pdfBytes, id);
        } catch (Exception e) {
            throw new ApiException("Failed to download invoice for order ID: " + id);
        }
    }

    private byte[] fetchAndDecodeInvoice(String url, RestTemplate restTemplate) throws ApiException {
        String base64Pdf = restTemplate.getForObject(url, String.class);
        return Base64.getDecoder().decode(base64Pdf);
    }

    private void saveInvoiceFile(byte[] pdfBytes, Long id, OrderPojo orderPojo) throws IOException {
        String filePath = "src/main/pdf/output" + id + ".pdf";
        Files.write(Paths.get(filePath), pdfBytes);
        orderPojo.setInvoicePath(filePath);
    }

    private ResponseEntity<byte[]> buildPdfResponse(byte[] pdfBytes, Long id) {
        return ResponseEntity.ok()
                .header("Content-Disposition", "attachment; filename=invoice_" + id + ".pdf")
                .contentType(org.springframework.http.MediaType.APPLICATION_PDF)
                .body(pdfBytes);
    }
}