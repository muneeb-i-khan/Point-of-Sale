package com.increff.pos.service;

import com.increff.pos.db.dao.DaySaleReportDao;
import com.increff.pos.db.dao.OrderDao;
import com.increff.pos.db.pojo.DaySaleReportPojo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
public class DaySaleReportService {
    @Autowired
    private DaySaleReportDao daySaleReportDao;

    @Autowired
    private OrderDao orderDao;

    @Autowired
    private OrderService orderService;

    @Transactional
    public void recordDailySales() {
        LocalDate yesterday = LocalDate.now().minusDays(1);

        int orderCount = orderDao.countOrdersByDate(yesterday);
        int itemSoldCount = orderDao.countItemsSoldByDate(yesterday);
        double revenue = orderDao.calculateRevenueByDate(yesterday);

        System.out.println("Generating Report for " + yesterday);
        System.out.println("Orders: " + orderCount + ", Items Sold: " + itemSoldCount + ", Revenue: " + revenue);

        if (orderCount > 0 || itemSoldCount > 0 || revenue > 0) {
            DaySaleReportPojo existingReport = daySaleReportDao.findByDate(yesterday);

            if (existingReport != null) {
                existingReport.setOrderCount(orderCount);
                existingReport.setItemSoldCount(itemSoldCount);
                existingReport.setRevenue(revenue);
                daySaleReportDao.update(existingReport);
                System.out.println("Updated existing report for " + yesterday);
            } else {
                DaySaleReportPojo report = new DaySaleReportPojo();
                report.setDate(yesterday);
                report.setOrderCount(orderCount);
                report.setItemSoldCount(itemSoldCount);
                report.setRevenue(revenue);
                daySaleReportDao.save(report);
                System.out.println("Report saved for " + yesterday);
            }
        } else {
            System.out.println("No sales data for " + yesterday + ", skipping report.");
        }
    }

    public List<DaySaleReportPojo> getReport(LocalDate start, LocalDate end) {
        return daySaleReportDao.findByDateRange(start, end);
    }

    public List<DaySaleReportPojo> getReport() {
        return daySaleReportDao.selectAll();
    }
}
