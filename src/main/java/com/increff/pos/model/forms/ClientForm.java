package com.increff.pos.model.forms;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter
@Setter
public class ClientForm {
    @NotBlank(message = "Client name can't be blank")
    private String name;
    @NotBlank(message = "Client description can't be blank")
    private String description;
}
