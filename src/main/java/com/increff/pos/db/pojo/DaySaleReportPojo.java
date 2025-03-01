package com.increff.pos.db.pojo;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Getter
@Setter
@Table(name = "pos_day_sale")
public class DaySaleReportPojo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "date", unique = true, nullable = false)
    private LocalDate date;

    @Column(name = "order_count", nullable = false)
    private int orderCount;

    @Column(name = "item_sold_count", nullable = false)
    private int itemSoldCount;

    @Column(name = "revenue", nullable = false)
    private Double revenue;
}
