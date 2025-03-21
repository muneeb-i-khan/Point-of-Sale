package com.increff.pos.job;

import com.increff.pos.flow.DaySaleReportFlow;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class DaySaleReportJob {
    @Autowired
    private DaySaleReportFlow daySaleReportFlow;
    // TODO: call dto --> hitting api at controller level
    @Scheduled(cron = "0 0 0 * * ?")
    public void generateDailySalesReport() {
        daySaleReportFlow.recordDailySales();
    }
}
