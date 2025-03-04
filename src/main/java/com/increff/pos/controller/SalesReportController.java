package com.increff.pos.controller;

import com.increff.pos.db.pojo.DaySaleReportPojo;
import com.increff.pos.db.pojo.SalesReportPojo;
import com.increff.pos.dto.SalesReportDto;
import com.increff.pos.model.data.SalesReportData;
import com.increff.pos.model.forms.SalesReportForm;
import com.increff.pos.service.ApiException;
import com.increff.pos.service.SalesReportService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
    public List<SalesReportData> getAllReports() throws ApiException {
        return salesReportDto.getAllSales();
    }

    @ApiOperation(value = "Get all sales report with pagination")
    @GetMapping("/sales/paginated")
    public ResponseEntity<Map<String, Object>> getAllPaginated(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "15") int pageSize) throws ApiException {
        Map<String, Object> response = salesReportDto.getAllSalesReportPaginated(page, pageSize);
        return ResponseEntity.ok(response);
    }
}
