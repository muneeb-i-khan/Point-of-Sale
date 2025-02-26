package com.increff.pos.dto;

import com.increff.pos.model.constants.Role;
import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Component;

import javax.validation.constraints.Email;
import javax.validation.constraints.Size;

@Component
@Getter
@Setter
public class UserDto {
    private Long id;
    @Email
    private String email;
    @Size(min=6, message = "Password should be of length greater than 6")
    private String password;
    private Role role;

    public UserDto() {}

    public UserDto(Long id, String email, Role role) {
        this.id = id;
        this.email = email;
        this.role = role;
    }

    public UserDto(Long id, String email, String password, Role role) {
        this.id = id;
        this.email = email;
        this.password = password;
        this.role = role;
    }
}
