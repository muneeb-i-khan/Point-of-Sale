package com.increff.pos.dto;

import com.increff.pos.db.pojo.CustomerPojo;
import com.increff.pos.db.pojo.OrderPojo;
import com.increff.pos.flow.OrderFlow;
import com.increff.pos.model.data.OrderData;
import com.increff.pos.model.forms.CustomerForm;
import com.increff.pos.util.ApiException;
import com.increff.pos.service.OrderService;
import com.increff.pos.model.forms.OrderForm.OrderItemForm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
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
        return orderFlow.downloadInvoice(id);
    }
}
