package com.increff.pos.controller;

import com.increff.pos.db.pojo.DaySaleReportPojo;
import com.increff.pos.service.DaySaleReportService;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@Api
@RestController
@RequestMapping("/api/report/day")
public class DaySaleReportController {
    @Autowired
    private DaySaleReportService daySaleReportService;

    @GetMapping("/sales/all")
    public ResponseEntity<List<DaySaleReportPojo>> getAllReports(){
        return ResponseEntity.ok(daySaleReportService.getReport());
    }

    @GetMapping("/sales")
    public ResponseEntity<List<DaySaleReportPojo>> getReport(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        return ResponseEntity.ok(daySaleReportService.getReport(startDate, endDate));
    }

    @PostMapping("/generate")
    public ResponseEntity<String> triggerDailySalesReport() {
        daySaleReportService.recordDailySales();
        return ResponseEntity.ok("Daily sales report generated.");
    }
}
