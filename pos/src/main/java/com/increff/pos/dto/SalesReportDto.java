package com.increff.pos.dto;

import com.increff.pos.db.pojo.SalesReportPojo;
import com.increff.pos.flow.SalesReportFlow;
import com.increff.pos.model.data.SalesReportData;
import com.increff.pos.model.forms.SalesReportForm;
import com.increff.pos.util.ApiException;
import com.increff.pos.service.SalesReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletResponse;
import java.time.ZonedDateTime;
import java.util.List;

@Component
public class SalesReportDto {

    @Autowired
    private SalesReportService salesReportService;
    @Autowired
    private SalesReportFlow salesReportFlow;

    public List<SalesReportData> getSalesReport(SalesReportForm salesReportForm) throws ApiException {
            String clientName = salesReportForm.getClientName();
            String description = salesReportForm.getDescription();
            ZonedDateTime startDate = salesReportForm.getStartDate();
            ZonedDateTime endDate = salesReportForm.getEndDate();

            if (startDate != null && endDate != null && startDate.isAfter(endDate)) {
                throw new ApiException("Start Date can't be after End Date");
            }

            List<SalesReportPojo> salesReportPojos = salesReportService.getSalesReport(clientName, description, startDate, endDate);
            return salesReportFlow.convert(salesReportPojos);
    }

    public List<SalesReportData> getAllSales() throws ApiException {
        List<SalesReportPojo> salesReportPojos = salesReportService.getAllSalesReport();
        return salesReportFlow.convert(salesReportPojos);
    }

    public List<SalesReportData> getAllSalesReportPaginated(int page, int pageSize, HttpServletResponse httpServletResponse) throws ApiException {
        List<SalesReportPojo> salesReportPojos = salesReportService.getAllSalesReportsPaginated(page, pageSize);
        Long totalSalesReport = salesReportService.getSalesReportCount();

        List<SalesReportData> salesReportDataList = salesReportFlow.convert(salesReportPojos);
        httpServletResponse.setHeader("totalSalesReport", totalSalesReport.toString());

        return salesReportDataList;
    }
}
