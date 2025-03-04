package com.increff.pos.model.forms;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Email;
import javax.validation.constraints.Size;

@Getter
@Setter
public class UserForm {
    @Email
    private String email;
    @Size(min=6, message = "Password should be of length greater than 6")
    private String password;
}
