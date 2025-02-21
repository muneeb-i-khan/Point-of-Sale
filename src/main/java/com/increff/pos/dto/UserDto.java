package com.increff.pos.dto;

import com.increff.pos.util.Role;

public class UserDto {
    private Long id;
    private String email;
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

    public Long getId() { return id; }
    public String getEmail() { return email; }
    public String getPassword() { return password; }
    public Role getRole() { return role; }
}
