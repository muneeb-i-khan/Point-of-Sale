package com.increff.pos.service;

import com.increff.pos.db.dao.DaySaleReportDao;
import com.increff.pos.db.dao.OrderDao;
import com.increff.pos.db.pojo.DaySaleReportPojo;
import com.increff.pos.db.pojo.InventoryPojo;
import com.increff.pos.flow.DaySaleReportFlow;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.ZonedDateTime;
import java.util.List;

@Service
public class DaySaleReportService {
    @Autowired
    private DaySaleReportDao daySaleReportDao;

    @Autowired
    private DaySaleReportFlow daySaleReportFlow;

    @Transactional
    //TODO: to refactor the code
    public void recordDailySales() {
        ZonedDateTime yesterday = ZonedDateTime.now().minusDays(1);

        int orderCount = daySaleReportFlow.getOrderCountByDate(yesterday);
        int itemSoldCount = daySaleReportFlow.getItemSoldCount(yesterday);
        double revenue = daySaleReportFlow.getRevenue(yesterday);

        //TODO: dont use syste,out, use logging instead
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

    public List<DaySaleReportPojo> getReport(ZonedDateTime start, ZonedDateTime end) {
        return daySaleReportDao.findByDateRange(start, end);
    }

    public List<DaySaleReportPojo> getAllDaySaleReportsPaginated(int page, int pageSize) {
        return daySaleReportDao.selectAllPaginated(page, pageSize);
    }

    public Long getDaySaleReportCount() {
        return daySaleReportDao.countDaySaleReportPojo();
    }


    public List<DaySaleReportPojo> getReport() {
        return daySaleReportDao.selectAll();
    }
}
