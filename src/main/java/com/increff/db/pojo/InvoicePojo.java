package com.increff.db.pojo;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Getter
@Setter
@Table(name = "invoice")
public class InvoicePojo {
    @Id
    private Long    orderId;
    private String  path;
}
