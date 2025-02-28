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

    @GetMapping("/{orderId}")
    public ResponseEntity<String> generateInvoice(@PathVariable Long orderId) {
        OrderData orderData = invoiceService.fetchOrderDetails(orderId);
        String pdfBase64 = invoiceService.generateInvoicePdf(orderData);
        return ResponseEntity.ok(pdfBase64);
    }
}
