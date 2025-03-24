package com.increff.pos.dto;

import com.increff.pos.db.pojo.CustomerPojo;
import com.increff.pos.db.pojo.OrderPojo;
import com.increff.pos.flow.OrderFlow;
import com.increff.pos.model.data.OrderData;
import com.increff.pos.model.forms.CustomerForm;
import com.increff.pos.util.ApiException;
import com.increff.pos.service.OrderService;
import com.increff.pos.model.forms.OrderForm.OrderItemForm;
import com.increff.pos.util.Constants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

@Component
public class OrderDto {
    @Autowired
    private OrderFlow orderFlow;

    @Autowired
    private OrderService orderService;

    @Autowired
    private CustomerDto customerDto;

    public OrderData addOrder(List<OrderItemForm> orderItemFormList, CustomerForm customerForm) throws ApiException {
        CustomerPojo customerPojo = customerDto.convert(customerForm);
        return orderFlow.addOrder(orderItemFormList, customerPojo);
    }

    public List<OrderData> getAllOrders() throws ApiException {
        return orderFlow.getAllOrders();
    }

    public OrderData getOrder(Long id) throws ApiException {
        return orderFlow.getOrder(id);
    }

    public List<OrderData> getAllOrdersPaginated(int page, int pageSize, HttpServletResponse httpServletResponse) throws ApiException {
        List<OrderPojo> orderPojos = orderService.getAllOrdersPaginated(page, pageSize);
        Long totalOrders = orderService.getOrderCount();

        List<OrderData> orderDataList = new ArrayList<>();
        for (OrderPojo p : orderPojos) {
            orderDataList.add(orderFlow.convert(p));
        }
        httpServletResponse.setHeader("totalOrders", totalOrders.toString());
        return orderDataList;
    }

    public byte[] downloadInvoice(Long id) throws ApiException{
        OrderPojo orderPojo = orderService.getOrderById(id);
        OrderData orderData = orderFlow.convert(orderPojo);
        byte[] existingInvoiceBytes = tryLoadExistingInvoiceBytes(orderPojo, id, orderData);
        if (existingInvoiceBytes != null) {
            return existingInvoiceBytes;
        }
        return generateAndSaveNewInvoiceBytes(orderPojo, id, orderData);
    }

    private byte[] tryLoadExistingInvoiceBytes(OrderPojo orderPojo, Long id, OrderData orderData) throws ApiException {
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

    private byte[] generateAndSaveNewInvoiceBytes(OrderPojo orderPojo, Long id, OrderData orderData) throws ApiException {
        String url = Constants.INVOICE_URL;
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
        String filePath = Constants.PDF_SAVE_PATH + id + ".pdf";
        Files.write(Paths.get(filePath), pdfBytes);
        orderPojo.setInvoicePath(filePath);
    }
}
