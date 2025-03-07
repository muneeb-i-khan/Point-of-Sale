package com.increff.pos.flow;

import com.increff.pos.db.pojo.SalesReportPojo;
import com.increff.pos.model.data.SalesReportData;
import com.increff.pos.service.ClientService;
import com.increff.pos.util.ApiException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class SalesReportFlow {

    @Autowired
    private ClientService clientService;

    public List<SalesReportData> convert(List<SalesReportPojo> salesReportPojos) throws ApiException {
        return salesReportPojos.stream()
                .map(this::convert)
                .collect(Collectors.toList());
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
