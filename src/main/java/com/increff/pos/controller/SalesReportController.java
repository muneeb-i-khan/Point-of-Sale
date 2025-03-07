package com.increff.pos.controller;

import com.increff.pos.dto.SalesReportDto;
import com.increff.pos.model.data.SalesReportData;
import com.increff.pos.model.forms.SalesReportForm;
import com.increff.pos.util.ApiException;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

@Api
@RestController
@RequestMapping("/api/report/sale")
public class SalesReportController {

    @Autowired
    private SalesReportDto salesReportDto;

    @ApiOperation(value = "Get sales report")
    @GetMapping("/filter")
    public List<SalesReportData> getSalesReport(
            @ModelAttribute SalesReportForm salesReportForm
            ) throws ApiException {
        return salesReportDto.getSalesReport(salesReportForm);
    }

    @ApiOperation(value = "Get all sales report")
    @GetMapping("/sales/all")
    public List<SalesReportData> getAllReports() throws ApiException {
        return salesReportDto.getAllSales();
    }

    @ApiOperation(value = "Get all sales report with pagination")
    @GetMapping("/sales/paginated")
    public List<SalesReportData> getAllPaginated(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "15") int pageSize, HttpServletResponse httpServletResponse) throws ApiException {
        return salesReportDto.getAllSalesReportPaginated(page, pageSize,httpServletResponse);
    }
}
