package com.increff.pos.dto;

import com.increff.pos.db.pojo.OrderPojo;
import com.increff.pos.flow.OrderFlowService;
import com.increff.pos.model.OrderData;
import com.increff.pos.model.SalesForm;
import com.increff.pos.model.SalesForm.SaleItem;
import com.increff.pos.service.ApiException;
import com.increff.pos.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
public class OrderDto {
    @Autowired
    private OrderFlowService orderFlowService;
    @Autowired
    private OrderService orderService;

    public void addOrder(SalesForm salesForm) throws ApiException {
        SalesForm mergedSalesForm = mergeDuplicateItems(salesForm);
        orderFlowService.createOrderAndUpdateInventory(mergedSalesForm);
    }

    public void updateOrder(SalesForm salesForm, Long id) throws ApiException {
        SalesForm mergedSalesForm = mergeDuplicateItems(salesForm);
        orderService.updateOrder(id, mergedSalesForm);
    }

    public List<OrderData> getAllOrders() {
        List<OrderPojo> list = orderService.getAllOrders();
        List<OrderData> orderDataList = new ArrayList<>();
        for (OrderPojo p : list) {
            orderDataList.add(convert(p));
        }
        return orderDataList;
    }

    public OrderData getOrder(Long id) throws ApiException {
        OrderPojo order = orderService.getOrderById(id);
        return new OrderData(order);
    }

    public void deleteOrder(Long id) throws ApiException {
        orderService.deleteOrder(id);
    }

    private OrderData convert(OrderPojo p) {
        OrderData data = new OrderData(p);
        data.setId(p.getId());
        data.setTotalAmount(p.getTotalAmount());
        data.setOrderDate(p.getOrderDate());
        return data;
    }

    private SalesForm mergeDuplicateItems(SalesForm salesForm) {
        Map<String, SaleItem> mergedItems = new HashMap<>();

        for (SaleItem item : salesForm.getItems()) {
            String barcode = item.getBarcode();
            if (mergedItems.containsKey(barcode)) {
                mergedItems.get(barcode).setQuantity(mergedItems.get(barcode).getQuantity() + item.getQuantity());
            } else {
                mergedItems.put(barcode, item);
            }
        }

        SalesForm mergedForm = new SalesForm();
        mergedForm.setItems(new ArrayList<>(mergedItems.values()));
        return mergedForm;
    }
}
