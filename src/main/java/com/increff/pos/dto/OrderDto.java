package com.increff.pos.dto;

import com.increff.pos.db.pojo.OrderPojo;
import com.increff.pos.db.pojo.ProductPojo;
import com.increff.pos.flow.OrderFlow;
import com.increff.pos.model.data.OrderData;
import com.increff.pos.model.data.ProductData;
import com.increff.pos.model.forms.CustomerForm;
import com.increff.pos.model.forms.OrderItemForm;
import com.increff.pos.service.ApiException;
import com.increff.pos.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

    public Map<String, Object> getAllOrdersPaginated(int page, int pageSize) throws ApiException {
        List<OrderPojo> orderPojos = orderService.getAllOrdersPaginated(page, pageSize);
        Long totalOrders = orderService.getOrderCount();

        List<OrderData> orderDataList = new ArrayList<>();
        for (OrderPojo p : orderPojos) {
            orderDataList.add(orderFlow.convert(p));
        }

        Map<String, Object> response = new HashMap<>();
        response.put("orders", orderDataList);
        response.put("totalOrders", totalOrders);
        return response;
    }

    public ResponseEntity<byte[]> downloadInvoice(Long id) throws ApiException{
        return orderService.downloadPdf(id);
    }
}
