package com.increff.pos.dto;

import com.increff.pos.db.pojo.ClientPojo;
import com.increff.pos.model.data.ClientData;
import com.increff.pos.model.forms.ClientForm;
import com.increff.pos.service.ApiException;
import com.increff.pos.service.ClientService;
import com.increff.pos.util.Normalize;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class ClientDto {
    @Autowired
    ClientService clientService;

    public void addClient(ClientForm clientForm) {
        ClientPojo clientPojo = convert(clientForm);
        clientService.addClient(clientPojo);
    }

    public ClientData getClient(Long id) throws ApiException {
        ClientPojo clientPojo = clientService.getCheck(id);
        return convert(clientPojo);
    }

    public List<ClientData> getAllClients() {
        List<ClientPojo> list = clientService.getAllClients();
        List<ClientData> list2 = new ArrayList<ClientData>();
        for(ClientPojo p : list) {
            list2.add(convert(p));
        }
        return list2;
    }

    public void updateClient(ClientForm clientForm, Long id) throws ApiException {
        ClientPojo clientPojo = new ClientPojo();
        clientPojo.setName(Normalize.normalizeName(Normalize.trimString(clientForm.getName())));
        clientPojo.setDescription(clientForm.getDescription());
        clientService.updateClient(id, clientPojo);
    }


    public ClientData convert(ClientPojo p) {
        ClientData clientData = new ClientData();
        clientData.setName(Normalize.normalizeName(Normalize.trimString(p.getName())));
        clientData.setDescription(p.getDescription());
        clientData.setId(p.getId());
        return clientData;
    }

    public ClientPojo convert(ClientForm ClientForm) {
        ClientPojo p = new ClientPojo();
        p.setName(Normalize.normalizeName(Normalize.trimString(ClientForm.getName())));
        p.setDescription(Normalize.trimString(ClientForm.getDescription()));
        return p;
    }

}
