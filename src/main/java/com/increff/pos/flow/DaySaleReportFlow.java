package com.increff.pos.flow;

import com.increff.pos.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.ZonedDateTime;

@Component
public class DaySaleReportFlow {
    @Autowired
    OrderService orderService;

    public int getOrderCountByDate(ZonedDateTime date) {
        return orderService.countOrdersByDate(date);
    }

    public double getRevenue(ZonedDateTime date) {
        return orderService.calculateRevenueByDate(date);
    }

    public int getItemSoldCount(ZonedDateTime dateTime) {
        return orderService.countItemsSoldByDate(dateTime);
    }
}
