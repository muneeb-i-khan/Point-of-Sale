package com.increff.pos.dto;

import com.increff.pos.flow.OrderFlow;
import com.increff.pos.model.data.OrderData;
import com.increff.pos.model.forms.OrderItemForm;
import com.increff.pos.service.ApiException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class OrderDto {
    @Autowired
    private OrderFlow orderFlow;

    public void addOrder(List<OrderItemForm> orderItemFormList) throws ApiException {
        orderFlow.addOrder(orderItemFormList);
    }

    public List<OrderData> getAllOrders() throws ApiException {
        return orderFlow.getAllOrders();
    }

    public OrderData getOrder(Long id) throws ApiException {
        return orderFlow.getOrder(id);
    }
}
