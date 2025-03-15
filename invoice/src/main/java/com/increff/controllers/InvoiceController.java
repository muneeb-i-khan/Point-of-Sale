package com.increff.controllers;

import com.increff.models.OrderData;
import com.increff.services.InvoiceService;
import com.increff.models.OrderData;
import com.increff.services.InvoiceService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/invoice")
public class InvoiceController {

    private final InvoiceService invoiceService;

    public InvoiceController(InvoiceService invoiceService) {
        this.invoiceService = invoiceService;
    }

    @PostMapping("/")
    public String generateInvoice(@RequestBody OrderData orderData) {
        return invoiceService.generateInvoicePdf(orderData);
    }
}
