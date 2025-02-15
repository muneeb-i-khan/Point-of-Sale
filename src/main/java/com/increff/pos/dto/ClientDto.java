package com.increff.pos.dto;

import com.increff.pos.db.pojo.ClientPojo;
import com.increff.pos.model.ClientData;
import com.increff.pos.model.ClientForm;

public class ClientDto {
    public static ClientData convert(ClientPojo p) {
        ClientData clientData = new ClientData();
        clientData.setName(p.getName());
        clientData.setDescription(p.getDescription());
        clientData.setId(p.getId());
        return clientData;
    }
    public static ClientPojo convert(ClientForm ClientForm) {
        ClientPojo p = new ClientPojo();
        p.setName(ClientForm.getName());
        p.setDescription(ClientForm.getDescription());
        return p;
    }
}
