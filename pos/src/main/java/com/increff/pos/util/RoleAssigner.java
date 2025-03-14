package com.increff.pos.util;


import com.increff.pos.model.constants.Role;

public class RoleAssigner {
    public static Role assignRole(String email) {
        if(email.matches("^[A-Za-z0-9._%+-]+@increff\\.com$")) {
            return Role.SUPERVISOR;
        }
        return Role.OPERATOR;
    }
}
