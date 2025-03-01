package com.increff.pos.dto;

import com.increff.pos.flow.OrderFlow;
import com.increff.pos.model.data.OrderData;
import com.increff.pos.model.forms.CustomerForm;
import com.increff.pos.model.forms.OrderItemForm;
import com.increff.pos.service.ApiException;
import com.increff.pos.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class OrderDto {
    @Autowired
    private OrderFlow orderFlow;

    @Autowired
    private OrderService orderService;

    public void addOrder(List<OrderItemForm> orderItemFormList, CustomerForm customerForm) throws ApiException {
        orderFlow.addOrder(orderItemFormList, customerForm);
    }

    public List<OrderData> getAllOrders() throws ApiException {
        return orderFlow.getAllOrders();
    }

    public OrderData getOrder(Long id) throws ApiException {
        return orderFlow.getOrder(id);
    }

    public ResponseEntity<byte[]> downloadInvoice(Long id) throws ApiException{
        return orderService.downloadPdf(id);
    }
}
