package com.increff.pos.job;

import com.increff.pos.dto.DaySaleReportDto;
import com.increff.pos.flow.DaySaleReportFlow;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class DaySaleReportJob {
    @Autowired
    private DaySaleReportDto daySaleReportDto;
    @Scheduled(cron = "0 0 0 * * ?")
    public void generateDailySalesReport() {
        daySaleReportDto.recordDailySales();
    }
}
