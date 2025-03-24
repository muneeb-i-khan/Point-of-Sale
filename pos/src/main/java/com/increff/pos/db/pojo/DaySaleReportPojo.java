package com.increff.pos.db.pojo;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.ZonedDateTime;

@Entity
@Getter
@Setter
@Table(name = "posDaySale", uniqueConstraints = @UniqueConstraint(columnNames = "date"))
public class DaySaleReportPojo extends AbstractPojo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private ZonedDateTime date;

    @Column(nullable = false)
    private int orderCount;

    @Column(nullable = false)
    private int itemSoldCount;

    @Column(nullable = false)
    private Double revenue;
}
