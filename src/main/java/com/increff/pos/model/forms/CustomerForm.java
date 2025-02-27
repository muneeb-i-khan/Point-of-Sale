package com.increff.pos.model.forms;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Table;

@Getter
@Setter
@Table(name = "customer")
public class CustomerForm {
    private String name;
    private String phone;
}
