package com.increff.pos.model.forms;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Getter
@Setter
public class ClientForm {
    @NotBlank(message = "Client name can't be blank")
    @Size(max = 255, message = "Client name can be maximum of size 255")
    private String name;
    @NotBlank(message = "Client description can't be blank")
    @Size(max = 255, message = "Client Desc can be maximum of size 255")
    private String description;
}
