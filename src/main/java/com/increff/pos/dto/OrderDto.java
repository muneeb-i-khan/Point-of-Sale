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

import javax.servlet.http.HttpServletResponse;
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

    public ResponseEntity<byte[]> downloadInvoice(Long id) throws ApiException{
        return orderService.downloadPdf(id);
    }
}
