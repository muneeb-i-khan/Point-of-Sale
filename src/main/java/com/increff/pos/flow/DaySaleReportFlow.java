package com.increff.pos.flow;

import com.increff.pos.db.pojo.DaySaleReportPojo;
import com.increff.pos.service.DaySaleReportService;
import com.increff.pos.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.ZonedDateTime;

@Component
public class DaySaleReportFlow {
    @Autowired
    OrderService orderService;

    @Autowired
    DaySaleReportService daySaleReportService;

    public int getOrderCountByDate(ZonedDateTime date) {
        return orderService.countOrdersByDate(date);
    }

    public double getRevenue(ZonedDateTime date) {
        return orderService.calculateRevenueByDate(date);
    }

    public int getItemSoldCount(ZonedDateTime dateTime) {
        return orderService.countItemsSoldByDate(dateTime);
    }

    @Transactional
    public void recordDailySales() {
        ZonedDateTime yesterday = ZonedDateTime.now().minusDays(1);
        int orderCount = getOrderCountByDate(yesterday);
        int itemSoldCount = getItemSoldCount(yesterday);
        double revenue = getRevenue(yesterday);
        DaySaleReportPojo report = daySaleReportService.generateReport(yesterday, orderCount, itemSoldCount, revenue);
        if (report != null) {
            daySaleReportService.saveOrUpdateReport(report);
        }
    }
}
