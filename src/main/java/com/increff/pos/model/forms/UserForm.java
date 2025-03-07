package com.increff.pos.model.forms;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Email;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@Getter
@Setter
public class UserForm {
    @Email(message = "Has to be a valid email")
    @Size(max = 255, message = "Email can be maximum of size 255")
    private String email;
    @Size(min=6, message = "Password should be of length greater than 6")
    @Pattern(
            regexp = "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]+$",
            message = "Password must contain at least one letter, one digit, and one special character (@$!%*?&)"
    )
    private String password;
}
