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

import java.time.LocalDate;
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
    public ResponseEntity<List<DaySaleReportPojo>> getAllReports(){
        return ResponseEntity.ok(daySaleReportService.getReport());
    }

    @GetMapping("/")
    public ResponseEntity<List<DaySaleReportPojo>> getReport(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        return ResponseEntity.ok(daySaleReportService.getReport(startDate, endDate));
    }

    @ApiOperation(value = "Get all day sales report with pagination")
    @GetMapping("/paginated")
    public ResponseEntity<Map<String, Object>> getAllPaginated(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "15") int pageSize) throws ApiException {
        List<DaySaleReportPojo> daySaleReportPojos = daySaleReportService.getAllDaySaleReportsPaginated(page, pageSize);
        Long toalDaySaleReports = daySaleReportService.getDaySaleReportCount();

        List<DaySaleReportPojo> daySaleReportPojoList = new ArrayList<>();
        for(DaySaleReportPojo p: daySaleReportPojos) {
            daySaleReportPojoList.add(p);
        }

        Map<String, Object> response = new HashMap<>();
        response.put("reports", daySaleReportPojoList);
        response.put("totalReports", toalDaySaleReports);
        return ResponseEntity.ok(response);
    }


    @PostMapping("/generate")
    public ResponseEntity<String> triggerDailySalesReport() {
        daySaleReportService.recordDailySales();
        return ResponseEntity.ok("Daily sales report generated.");
    }
}
