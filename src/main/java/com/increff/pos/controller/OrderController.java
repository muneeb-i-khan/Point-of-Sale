package com.increff.pos.controller;

import com.increff.pos.flow.OrderFlowService;
import com.increff.pos.model.SalesForm;
import com.increff.pos.service.ApiException;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
@Api
@RestController
public class OrderController {

    @Autowired
    private OrderFlowService orderFlowService;

    @ApiOperation(value = "Create an order with multiple items")
    @PostMapping("/api/order")
    public void createOrder(@RequestBody SalesForm salesForm) throws ApiException {
        orderFlowService.createOrderAndUpdateInventory(salesForm);
    }
}
