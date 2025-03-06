package com.increff.pos.service;

import com.increff.pos.db.dao.ClientDao;
import com.increff.pos.db.dao.OrderDao;
import com.increff.pos.db.dao.OrderItemDao;
import com.increff.pos.db.dao.SalesReportDao;
import com.increff.pos.db.pojo.*;
import com.sun.org.apache.xpath.internal.operations.Or;
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
import java.util.Objects;

@Service
@Transactional(rollbackOn = ApiException.class)
public class OrderService {

    @Autowired
    private OrderItemDao orderItemDao;

    @Autowired
    private OrderDao orderDao;

    @Autowired
    private ProductService productService;

    @Autowired
    private InventoryService inventoryService;

    @Autowired
    private SalesReportDao salesReportDao;

    @Autowired
    private ClientDao clientDao;

    @Autowired
    private CustomerService customerService;

    @Transactional(rollbackOn = ApiException.class)
    public OrderPojo createOrder(List<OrderItemPojo> orderItemPojoList, CustomerPojo customerPojo) throws ApiException {
        OrderPojo order = new OrderPojo();
        order.setOrderDate(ZonedDateTime.now());
        double totalAmt = 0.0;
        customerService.addCustomer(customerPojo);
        order.setCustomerId(customerPojo.getId());
        order.setInvoicePath("");
        orderDao.add(order);

        for (OrderItemPojo orderItem : orderItemPojoList) {
            orderItem.setOrderId(order.getId());
            orderItemDao.add(orderItem);
            ProductPojo productPojo = productService.getProduct(orderItem.getProdId());
            InventoryPojo inventoryPojo = inventoryService.getInventoryByBarcode(productPojo.getBarcode());

            if (orderItem.getQuantity() <= 0) {
                throw new ApiException("Quantity can't be negative");
            }
            if (inventoryPojo.getQuantity() < orderItem.getQuantity()) {
                throw new ApiException("Insufficient stock for product: " + productPojo.getName());
            }

            inventoryPojo.setQuantity(inventoryPojo.getQuantity() - orderItem.getQuantity());
            totalAmt += orderItem.getSellingPrice() * orderItem.getQuantity();
        }
        order.setTotalAmount(totalAmt);
        updateSalesReport(order, orderItemPojoList, totalAmt);

        return order;
    }

    private void updateSalesReport(OrderPojo order, List<OrderItemPojo> orderItems, double totalAmount) throws ApiException {
        Long clientId = productService.getProduct(orderItems.get(0).getProdId()).getClientId();
        SalesReportPojo report = salesReportDao.findByClientAndDate(clientId, order.getOrderDate());

        long totalItemsSold = orderItems.stream().mapToLong(OrderItemPojo::getQuantity).sum();

        if (report == null) {
            report = new SalesReportPojo();
            report.setClientId(clientId);
            report.setDate(order.getOrderDate());
            report.setItemSold(totalItemsSold);
            report.setRevenue((long) totalAmount);
            salesReportDao.add(report);
        } else {
            report.setItemSold(report.getItemSold() + totalItemsSold);
            report.setRevenue(report.getRevenue() + (long) totalAmount);
            salesReportDao.update(report);
        }
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
        if (orderPojo.getInvoicePath() != null && !orderPojo.getInvoicePath().isEmpty()) {
            File pdfFile = new File(orderPojo.getInvoicePath());
            if (pdfFile.exists()) {
                try {
                    byte[] pdfBytes = Files.readAllBytes(pdfFile.toPath());
                    return ResponseEntity.ok()
                            .header("Content-Disposition", "attachment; filename=invoice_" + id + ".pdf")
                            .contentType(org.springframework.http.MediaType.APPLICATION_PDF)
                            .body(pdfBytes);
                } catch (IOException e) {
                    throw new ApiException("Failed to read existing invoice file for order ID: " + id);
                }
            }

        }

        String url = "http://localhost:9001/invoice/api/invoice/" + id;
        RestTemplate restTemplate = new RestTemplate();
        try {
            String base64Pdf = restTemplate.getForObject(url, String.class);
            byte[] pdfBytes = Base64.getDecoder().decode(base64Pdf);

            String filePath = "src/main/pdf/output" + id + ".pdf";
            Files.write(Paths.get(filePath), pdfBytes);
            orderPojo.setInvoicePath(filePath);

            return ResponseEntity.ok()
                    .header("Content-Disposition", "attachment; filename=invoice_" + id + ".pdf")
                    .contentType(org.springframework.http.MediaType.APPLICATION_PDF)
                    .body(pdfBytes);
        } catch (Exception e) {
            throw new ApiException("Failed to download invoice for order ID: " + id);
        }
    }

}

