package com.increff.pos.model.data;

import com.increff.pos.model.constants.Role;
import com.increff.pos.model.forms.UserForm;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserData extends UserForm {
    private Long id;
    private Role role;

    public UserData(Long id, String email, Role role) {
        this.setId(id);
        this.setEmail(email);
        this.setRole(role);
    }
}
