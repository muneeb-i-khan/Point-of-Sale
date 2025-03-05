package com.increff.pos.controller;

import com.increff.pos.db.pojo.DaySaleReportPojo;
import com.increff.pos.db.pojo.InventoryPojo;
import com.increff.pos.model.data.InventoryData;
import com.increff.pos.service.ApiException;
import com.increff.pos.service.DaySaleReportService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Api
@RestController
@RequestMapping("/api/report/day/sales")
public class DaySaleReportController {
    @Autowired
    private DaySaleReportService daySaleReportService;

    @GetMapping("/all")
    public List<DaySaleReportPojo> getAllReports(){
        return daySaleReportService.getReport();
    }

    @GetMapping("/")
    public List<DaySaleReportPojo> getReport(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) ZonedDateTime startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) ZonedDateTime endDate) {
        return daySaleReportService.getReport(startDate, endDate);
    }

    @CrossOrigin(origins = "http://localhost:4200", exposedHeaders = "totalDaySaleReport")
    @ApiOperation(value = "Get all day sales report with pagination")
    @GetMapping("/paginated")
    public List<DaySaleReportPojo> getAllPaginated(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "15") int pageSize, HttpServletResponse httpServletResponse) throws ApiException {
        List<DaySaleReportPojo> daySaleReportPojos = daySaleReportService.getAllDaySaleReportsPaginated(page, pageSize);
        Long toalDaySaleReports = daySaleReportService.getDaySaleReportCount();

        List<DaySaleReportPojo> daySaleReportPojoList = new ArrayList<>();
        for(DaySaleReportPojo p: daySaleReportPojos) {
            daySaleReportPojoList.add(p);
        }

        httpServletResponse.setHeader("totalDaySaleReport", toalDaySaleReports.toString());
        return daySaleReportPojoList;
    }


    @PostMapping("/generate")
    public void triggerDailySalesReport() {
        daySaleReportService.recordDailySales();
    }
}
