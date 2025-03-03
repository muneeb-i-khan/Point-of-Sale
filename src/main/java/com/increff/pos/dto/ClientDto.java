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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

    public Map<String, Object> getAllClientsPaginated(int page, int pageSize) {
        List<ClientPojo> clientPojos = clientService.getAllClientsPaginated(page, pageSize);
        Long totalClients = clientService.getClientCount();

        List<ClientData> clientDataList = new ArrayList<>();
        for (ClientPojo p : clientPojos) {
            clientDataList.add(convert(p));
        }

        Map<String, Object> response = new HashMap<>();
        response.put("clients", clientDataList);
        response.put("totalClients", totalClients);
        return response;
    }


    public void updateClient(ClientForm clientForm, Long id) throws ApiException {
        ClientPojo clientPojo = new ClientPojo();
        clientPojo.setName(Normalize.normalizeName(clientForm.getName().trim()));
        clientPojo.setDescription(clientForm.getDescription());
        clientService.updateClient(id, clientPojo);
    }


    public ClientData convert(ClientPojo p) {
        ClientData clientData = new ClientData();
        clientData.setName(Normalize.normalizeName(p.getName().trim()));
        clientData.setDescription(p.getDescription());
        clientData.setId(p.getId());
        return clientData;
    }

    public ClientPojo convert(ClientForm ClientForm) {
        ClientPojo p = new ClientPojo();
        p.setName(Normalize.normalizeName((ClientForm.getName().trim())));
        p.setDescription(ClientForm.getDescription().trim());
        return p;
    }

}
