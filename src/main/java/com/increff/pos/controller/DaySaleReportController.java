package com.increff.pos.controller;

import com.increff.pos.db.pojo.DaySaleReportPojo;
import com.increff.pos.util.ApiException;
import com.increff.pos.service.DaySaleReportService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;

@Api
@RestController
@RequestMapping("/api/report/day/sales")
public class DaySaleReportController {

    @Autowired
    private DaySaleReportService daySaleReportService;

    @ApiOperation(value = "Get a report")
    @GetMapping("/")
    public List<DaySaleReportPojo> getReport(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) ZonedDateTime startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) ZonedDateTime endDate) {
        return daySaleReportService.getReport(startDate, endDate);
    }

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


    @ApiOperation(value = "Trigger report generation")
    @PostMapping("/generate")
    public void triggerDailySalesReport() {
        daySaleReportService.recordDailySales();
    }
}
