package com.increff.pos.service;

import com.increff.pos.db.dao.DaySaleReportDao;
import com.increff.pos.db.pojo.DaySaleReportPojo;
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
    public void recordDailySales() {
        ZonedDateTime yesterday = ZonedDateTime.now().minusDays(1);

        DaySaleReportPojo report = generateReport(yesterday);
        if (report != null) {
            saveOrUpdateReport(report);
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

    private DaySaleReportPojo generateReport(ZonedDateTime date) {
        int orderCount = daySaleReportFlow.getOrderCountByDate(date);
        int itemSoldCount = daySaleReportFlow.getItemSoldCount(date);
        double revenue = daySaleReportFlow.getRevenue(date);

        if (orderCount == 0 && itemSoldCount == 0 && revenue == 0) {
            return null;
        }

        DaySaleReportPojo report = new DaySaleReportPojo();
        report.setDate(date);
        report.setOrderCount(orderCount);
        report.setItemSoldCount(itemSoldCount);
        report.setRevenue(revenue);
        return report;
    }

    private void saveOrUpdateReport(DaySaleReportPojo report) {
        DaySaleReportPojo existingReport = daySaleReportDao.findByDate(report.getDate());
        if (existingReport != null) {
            existingReport.setOrderCount(report.getOrderCount());
            existingReport.setItemSoldCount(report.getItemSoldCount());
            existingReport.setRevenue(report.getRevenue());
            daySaleReportDao.update(existingReport);
        } else {
            daySaleReportDao.save(report);
        }
    }

}
