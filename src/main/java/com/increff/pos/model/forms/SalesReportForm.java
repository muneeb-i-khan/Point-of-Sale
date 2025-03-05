package com.increff.pos.model.forms;

import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.Column;
import javax.validation.constraints.NotBlank;
import java.time.ZonedDateTime;

@Getter
@Setter
public class SalesReportForm {
    private String clientName;
    private String description;
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private ZonedDateTime startDate;
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private ZonedDateTime endDate;
}