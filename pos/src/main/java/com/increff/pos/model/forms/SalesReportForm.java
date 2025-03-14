package com.increff.pos.model.forms;

import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.Column;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.time.ZonedDateTime;

@Getter
@Setter
public class SalesReportForm {
    @Size(max = 255, message = "Client name can be maximum of size 255")
    private String clientName;
    @Size(max = 255, message = "Client Desc can be maximum of size 255")
    private String description;
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private ZonedDateTime startDate;
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private ZonedDateTime endDate;
}