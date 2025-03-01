package com.increff.pos.controller;

import com.increff.pos.db.pojo.DaySaleReportPojo;
import com.increff.pos.db.pojo.SalesReportPojo;
import com.increff.pos.dto.SalesReportDto;
import com.increff.pos.model.data.SalesReportData;
import com.increff.pos.model.forms.SalesReportForm;
import com.increff.pos.service.ApiException;
import com.increff.pos.service.SalesReportService;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Api
@RestController
@RequestMapping("/api/report/sale")
public class SalesReportController {

    @Autowired
    private SalesReportDto salesReportDto;
    @GetMapping("/filter")
    public List<SalesReportData> getSalesReport(
            @ModelAttribute SalesReportForm salesReportForm
            ) throws ApiException {
        return salesReportDto.getSalesReport(salesReportForm);
    }

    @GetMapping("/sales/all")
    public ResponseEntity<List<SalesReportData>> getAllReports() throws ApiException {
        return ResponseEntity.ok(salesReportDto.getAllSales());
    }
}
