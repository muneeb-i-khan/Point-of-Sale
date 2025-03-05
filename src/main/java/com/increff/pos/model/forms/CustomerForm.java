package com.increff.pos.model.forms;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Getter
@Setter
@Table(name = "customer")
public class CustomerForm {
    @NotBlank(message = "Customer name can't be blank")
    private String name;
    @Size(min = 10, max = 10, message = "Phone number must be of length 10")
    private String phone;
}
