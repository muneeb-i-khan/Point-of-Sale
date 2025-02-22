package com.increff.pos.controller;

import com.increff.pos.dto.OrderDto;
import com.increff.pos.model.data.OrderData;
import com.increff.pos.model.forms.SalesForm;
import com.increff.pos.service.ApiException;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Api
@RestController
public class OrderController {

    @Autowired
    OrderDto orderDto;

    @ApiOperation(value = "Create an order with multiple items")
    @PostMapping("/api/order")
    public void addOrder(@RequestBody SalesForm salesForm) throws ApiException {
        orderDto.addOrder(salesForm);
    }

    @ApiOperation(value = "Get all orders")
    @GetMapping("/api/order")
    public List<OrderData> getAllOrders() throws ApiException {
        return orderDto.getAllOrders();
    }

    @ApiOperation(value = "Get order by ID")
    @GetMapping("/api/order/{id}")
    public OrderData getOrderById(@PathVariable Long id) throws ApiException {
        return orderDto.getOrder(id);
    }

    @ApiOperation(value = "Delete an order")
    @DeleteMapping("/api/order/{id}")
    public void deleteOrder(@PathVariable Long id) throws ApiException {
        orderDto.deleteOrder(id);
    }

    @ApiOperation(value = "Update an order")
    @PutMapping("/api/order/{id}")
    public void updateOrder(@PathVariable Long id, @RequestBody SalesForm salesForm) throws ApiException {
        orderDto.updateOrder(salesForm, id);
    }

}
