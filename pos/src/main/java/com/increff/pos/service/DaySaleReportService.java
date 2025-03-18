package com.increff.pos.service;

import com.increff.pos.db.dao.DaySaleReportDao;
import com.increff.pos.db.pojo.DaySaleReportPojo;
import com.increff.pos.flow.DaySaleReportFlow;
import com.increff.pos.util.ApiException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.ZonedDateTime;
import java.util.List;

@Service
@Transactional(rollbackOn = ApiException.class)
public class DaySaleReportService {
    @Autowired
    private DaySaleReportDao daySaleReportDao;

    public List<DaySaleReportPojo> getReport(ZonedDateTime start, ZonedDateTime end) {
        if(start.isAfter(end)) {
            throw new ApiException("Start date can't be after end date");
        }
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

    public DaySaleReportPojo generateReport(ZonedDateTime date, int orderCount, int itemSoldCount, double revenue) {

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

    public void saveOrUpdateReport(DaySaleReportPojo report) {
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
