package com.increff.pos.service;

import com.increff.pos.db.dao.OrderDao;
import com.increff.pos.db.dao.OrderItemDao;
import com.increff.pos.db.pojo.*;
import com.increff.pos.model.data.OrderData;
import com.increff.pos.util.ApiException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
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

    public void createOrderItem(OrderItemPojo orderItemPojo) {
        orderItemDao.add(orderItemPojo);
    }

    public void createOrder(OrderPojo orderPojo) {
            orderDao.add(orderPojo);
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

    public List<OrderItemPojo> getItemsByOrderId(Long id) {
        return orderItemDao.getItemsByOrderId(id);
    }

    public void validateOrderItemQuantity(OrderItemPojo orderItem, ProductPojo productPojo, InventoryPojo inventoryPojo) throws ApiException {
        if (orderItem.getQuantity() <= 0) {
            throw new ApiException("Quantity can't be negative");
        }
        if (inventoryPojo.getQuantity() < orderItem.getQuantity()) {
            throw new ApiException("Insufficient stock for product: " + productPojo.getName());
        }
    }

    public byte[] tryLoadExistingInvoiceBytes(OrderPojo orderPojo, Long id, OrderData orderData) throws ApiException {
        if (orderPojo.getInvoicePath() != null && !orderPojo.getInvoicePath().isEmpty()) {
            File pdfFile = new File(orderPojo.getInvoicePath());
            if (pdfFile.exists()) {
                try {
                    return Files.readAllBytes(pdfFile.toPath());
                } catch (IOException e) {
                    throw new ApiException("Failed to read existing invoice file for order ID: " + id);
                }
            }
        }
        return null;
    }

    public byte[] generateAndSaveNewInvoiceBytes(OrderPojo orderPojo, Long id, OrderData orderData) throws ApiException {
        String url = "http://localhost:9001/invoice/api/invoice/";
        RestTemplate restTemplate = new RestTemplate();
        try {
            byte[] pdfBytes = fetchAndDecodeInvoice(url, restTemplate, orderData);
            saveInvoiceFile(pdfBytes, id, orderPojo);
            return pdfBytes;
        } catch (Exception e) {
            throw new ApiException("Failed to generate invoice for order ID: " + id);
        }
    }

    private byte[] fetchAndDecodeInvoice(String url, RestTemplate restTemplate, OrderData orderData) throws ApiException {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<OrderData> request = new HttpEntity<>(orderData, headers);

        ResponseEntity<String> response = restTemplate.postForEntity(url, request, String.class);
        String base64Pdf = response.getBody();

        if (base64Pdf == null) {
            throw new ApiException("Failed to receive invoice PDF from server");
        }

        return Base64.getDecoder().decode(base64Pdf);
    }

    private void saveInvoiceFile(byte[] pdfBytes, Long id, OrderPojo orderPojo) throws IOException {
        String filePath = "src/main/pdf/output" + id + ".pdf";
        Files.write(Paths.get(filePath), pdfBytes);
        orderPojo.setInvoicePath(filePath);
    }

//    private byte[] buildPdfResponse(byte[] pdfBytes, Long id) {
//        return ResponseEntity.ok()
//                .header("Content-Disposition", "attachment; filename=invoice_" + id + ".pdf")
//                .contentType(MediaType.APPLICATION_PDF)
//                .body(pdfBytes);
//    }
}