package com.increff.pos.model.data;

import com.increff.pos.model.forms.ClientForm;

public class ClientData extends ClientForm {
    private Long        id;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
