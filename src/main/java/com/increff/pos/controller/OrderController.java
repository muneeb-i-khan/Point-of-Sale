package com.increff.pos.controller;

import com.increff.pos.dto.OrderDto;
import com.increff.pos.model.data.OrderData;
import com.increff.pos.model.forms.OrderItemForm;
import com.increff.pos.service.ApiException;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Api
@RestController
@RequestMapping("/api/order")
public class OrderController {

    @Autowired
    OrderDto orderDto;

    @ApiOperation(value = "Create an order with multiple items")
    @PostMapping
    public void addOrder(@RequestBody List<OrderItemForm> orderItemForm) throws ApiException {
        orderDto.addOrder(orderItemForm);
    }

    @ApiOperation(value = "Get all orders")
    @GetMapping
    public List<OrderData> getAllOrders() throws ApiException {
        return orderDto.getAllOrders();
    }

    @ApiOperation(value = "Get order by ID")
    @GetMapping("/{id}")
    public OrderData getOrderById(@PathVariable Long id) throws ApiException {
        return orderDto.getOrder(id);
    }

}
