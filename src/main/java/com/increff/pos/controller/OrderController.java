package com.increff.pos.controller;

import com.increff.pos.dto.OrderDto;
import com.increff.pos.model.data.OrderData;
import com.increff.pos.model.forms.OrderForm;
import com.increff.pos.service.ApiException;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Collections;
import java.util.List;
import java.util.Map;

@Api
@RestController
@RequestMapping("/api/order")
public class OrderController {

    @Autowired
    private OrderDto orderDto;

    @PostMapping
    public ResponseEntity<Map<String, String>> addOrder(@Valid @RequestBody OrderForm orderForm) throws ApiException {
        orderDto.addOrder(orderForm.getOrderItems(), orderForm.getCustomer());
        return ResponseEntity.ok(Collections.singletonMap("message", "Order created successfully"));
    }


    @ApiOperation(value = "Get all orders")
    @GetMapping
    public ResponseEntity<List<OrderData>> getAllOrders() throws ApiException {
        List<OrderData> orders = orderDto.getAllOrders();
        return ResponseEntity.ok(orders);
    }

    @ApiOperation(value = "Get order by ID")
    @GetMapping("/{id}")
    public ResponseEntity<OrderData> getOrderById(@PathVariable Long id) throws ApiException {
        OrderData order = orderDto.getOrder(id);
        return ResponseEntity.ok(order);
    }
}
