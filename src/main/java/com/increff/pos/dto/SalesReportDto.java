package com.increff.pos.dto;

import com.increff.pos.db.pojo.SalesReportPojo;
import com.increff.pos.model.data.SalesReportData;
import com.increff.pos.model.forms.SalesReportForm;
import com.increff.pos.service.ApiException;
import com.increff.pos.service.ClientService;
import com.increff.pos.service.SalesReportService;
import net.bytebuddy.asm.Advice;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Component
public class SalesReportDto {
    @Autowired
    private SalesReportService salesReportService;
    @Autowired
    private ClientService clientService;

    public List<SalesReportData> getSalesReport(SalesReportForm salesReportForm) throws ApiException {
        String clientName = salesReportForm.getClientName();
        String description = salesReportForm.getDescription();
        LocalDate startDate = salesReportForm.getStartDate();
        LocalDate endDate = salesReportForm.getEndDate();

        List<SalesReportPojo> salesReportPojos = salesReportService.getSalesReport(clientName, description, startDate, endDate);
        List<SalesReportData> salesReportDataList = new ArrayList<>();

        for (SalesReportPojo pojo : salesReportPojos) {
            salesReportDataList.add(convert(pojo));
        }

        return salesReportDataList;
    }


    public List<SalesReportData> getAllSales() throws ApiException {
        List<SalesReportPojo> salesReportPojos = salesReportService.getAllSalesReport();
        List<SalesReportData> salesReportDataList = new ArrayList<>();

        for (SalesReportPojo pojo : salesReportPojos) {
            salesReportDataList.add(convert(pojo));
        }

        return salesReportDataList;
    }

    private SalesReportData convert(SalesReportPojo salesReportPojo) throws ApiException {
        SalesReportData salesReportData = new SalesReportData();
        salesReportData.setClientName(clientService.getClient(salesReportPojo.getClientId()).getName());
        salesReportData.setDescription(clientService.getClient(salesReportPojo.getClientId()).getDescription());
        salesReportData.setQuantity(salesReportPojo.getItemSold());
        salesReportData.setRevenue(salesReportPojo.getRevenue());
        return salesReportData;
    }
}

