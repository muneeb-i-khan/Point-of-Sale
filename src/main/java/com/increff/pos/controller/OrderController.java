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
import org.springframework.web.client.RestTemplate;

import javax.validation.Valid;
import java.util.Base64;
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
    public void addOrder(@Valid @RequestBody OrderForm orderForm) throws ApiException {
        orderDto.addOrder(orderForm.getOrderItems(), orderForm.getCustomer());
    }


    @ApiOperation(value = "Get all orders")
    @GetMapping
    public List<OrderData> getAllOrders() throws ApiException {
        return orderDto.getAllOrders();
    }


    @ApiOperation(value = "Get all orders with pagination")
    @GetMapping("/paginated")
    public ResponseEntity<Map<String, Object>> getAllPaginated(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "15") int pageSize) throws ApiException {
        Map<String, Object> response = orderDto.getAllOrdersPaginated(page, pageSize);
        return ResponseEntity.ok(response);
    }

    @ApiOperation(value = "Get order by ID")
    @GetMapping("/{id}")
    public OrderData getOrderById(@PathVariable Long id) throws ApiException {
        return orderDto.getOrder(id);
    }

    @ApiOperation(value = "Download Invoice PDF")
    @GetMapping("/download/{id}")
    public ResponseEntity<byte[]> downloadInvoice(@PathVariable Long id) throws ApiException {
        return orderDto.downloadInvoice(id);
    }
}
