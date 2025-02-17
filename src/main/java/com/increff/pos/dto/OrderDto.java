package com.increff.pos.dto;

import com.increff.pos.db.pojo.OrderPojo;
import com.increff.pos.flow.OrderFlowService;
import com.increff.pos.model.OrderData;
import com.increff.pos.model.SalesForm;
import com.increff.pos.service.ApiException;
import com.increff.pos.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class OrderDto {
    @Autowired
    private OrderFlowService orderFlowService;
    @Autowired
    private OrderService orderService;

    public void addOrder(SalesForm salesForm) throws ApiException {
        orderFlowService.createOrderAndUpdateInventory(salesForm);
    }

    public List<OrderData> getAllOrders() {
        List<OrderPojo> list = orderService.getAllOrders();
        List<OrderData> list2 = new ArrayList<OrderData>();
        for(OrderPojo p : list) {
            list2.add(convert(p));
        }
        return list2;
    }

    public OrderData getOrder(Long id) throws ApiException {
        OrderPojo order = orderService.getOrderById(id);
        return new OrderData(order);
    }

    public void deleteOrder(Long id) throws ApiException {
        orderService.deleteOrder(id);
    }

    public void updateOrder(SalesForm salesForm, Long id) throws ApiException {
        orderService.updateOrder(id, salesForm);
    }

    public OrderData convert(OrderPojo p) {
        OrderData data = new OrderData(p);
        data.setId(p.getId());
        data.setTotalAmount(p.getTotalAmount());
        data.setOrderDate(p.getOrderDate());
        return data;
    }
}
