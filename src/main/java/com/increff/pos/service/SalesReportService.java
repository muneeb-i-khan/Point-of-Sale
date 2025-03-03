package com.increff.pos.service;

import com.increff.pos.db.dao.SalesReportDao;
import com.increff.pos.db.pojo.DaySaleReportPojo;
import com.increff.pos.db.pojo.SalesReportPojo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDate;
import java.util.List;

@Service
@Transactional
public class SalesReportService {

    @Autowired
    private SalesReportDao salesReportDao;

    public List<SalesReportPojo> getSalesReport( String clientName, String description, LocalDate startDate, LocalDate endDate) {
        return salesReportDao.filterReports(clientName, description, startDate, endDate);
    }

    public List<SalesReportPojo> getAllSalesReport() {
        return salesReportDao.selectAll();
    }

    public List<SalesReportPojo> getAllSalesReportsPaginated(int page, int pageSize) {
        return salesReportDao.selectAllPaginated(page, pageSize);
    }
    public Long getSalesReportCount() {
        return salesReportDao.countSalesReportPojo();
    }
}
