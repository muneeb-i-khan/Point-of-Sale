package com.increff.pos.model.forms;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter
@Setter
public class ClientForm {
    @NotBlank
    @NotNull
    private String      name;
    @NotBlank
    @NotNull
    private String      description;
}
