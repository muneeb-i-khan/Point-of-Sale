package com.increff.pos.controller;

import com.increff.pos.dto.OrderDto;
import com.increff.pos.model.data.OrderData;
import com.increff.pos.model.forms.OrderForm;
import com.increff.pos.util.ApiException;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.List;

@Api
@RestController
@RequestMapping("/api/order")
public class OrderController {

    @Autowired
    private OrderDto orderDto;

    @ApiOperation(value = "Add an order")
    @PostMapping
    public OrderData addOrder(@Valid @RequestBody OrderForm orderForm) throws ApiException {
        return orderDto.addOrder(orderForm.getOrderItems(), orderForm.getCustomer());
    }


    @ApiOperation(value = "Get all orders")
    @GetMapping
    public List<OrderData> getAllOrders() throws ApiException {
        return orderDto.getAllOrders();
    }

    @CrossOrigin(origins = "http://localhost:4200", exposedHeaders = "totalOrders")
    @ApiOperation(value = "Get all orders with pagination")
    @GetMapping("/paginated")
    public List<OrderData> getAllPaginated(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "15") int pageSize, HttpServletResponse httpServletResponse) throws ApiException {
        return orderDto.getAllOrdersPaginated(page, pageSize, httpServletResponse);
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
