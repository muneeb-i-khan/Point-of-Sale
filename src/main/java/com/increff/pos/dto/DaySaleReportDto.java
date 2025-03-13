package com.increff.pos.dto;

import com.increff.pos.db.pojo.DaySaleReportPojo;
import com.increff.pos.db.pojo.ProductPojo;
import com.increff.pos.service.DaySaleReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;

@Component
public class DaySaleReportDto {
    @Autowired
    private DaySaleReportService daySaleReportService;

    public List<DaySaleReportPojo> getAllPaginated(int page, int pageSize, HttpServletResponse httpServletResponse) {
        List<DaySaleReportPojo> daySaleReportPojos = daySaleReportService.getAllDaySaleReportsPaginated(page, pageSize);
        Long toalDaySaleReports = daySaleReportService.getDaySaleReportCount();

        List<DaySaleReportPojo> daySaleReportPojoList = new ArrayList<>();
        for(DaySaleReportPojo p: daySaleReportPojos) {
            daySaleReportPojoList.add(p);
        }
        httpServletResponse.setHeader("totalDaySaleReport", toalDaySaleReports.toString());
        return daySaleReportPojoList;
    }
}
