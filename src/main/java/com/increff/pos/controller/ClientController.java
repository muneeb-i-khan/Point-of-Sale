package com.increff.pos.controller;

import com.increff.pos.dto.ClientDto;
import com.increff.pos.model.ClientData;
import com.increff.pos.model.ClientForm;
import com.increff.pos.db.pojo.ClientPojo;
import com.increff.pos.service.ApiException;
import com.increff.pos.service.ClientService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@Api
@RestController
public class ClientController {
    @Autowired
    ClientService clientService;
    @ApiOperation(value = "Post a client")
    @RequestMapping(path="/api/client/", method = RequestMethod.POST)
    public void addClient(@RequestBody ClientForm ClientForm) {
        ClientPojo p = ClientDto.convert(ClientForm);
        clientService.addClient(p);
    }
    @ApiOperation(value = "Delete a client")
    @RequestMapping(path="/api/client/{id}", method = RequestMethod.DELETE)
    public void delete(@PathVariable Long id) throws ApiException  {
        clientService.getCheck(id);
        clientService.deleteClient(id);
    }
    @ApiOperation(value = "Get a client based on it's Id")
    @RequestMapping(path = "/api/client/{id}", method = RequestMethod.GET)
    public ClientData get(@PathVariable Long id) throws ApiException {
        clientService.getCheck(id);
        ClientPojo p = clientService.getClient(id);
        return ClientDto.convert(p);
    }

    @ApiOperation(value = "Get all clients")
    @RequestMapping(path = "/api/client", method = RequestMethod.GET)
    public List<ClientData> getAll() {
        List<ClientPojo> list = clientService.getAllClients();
        List<ClientData> list2 = new ArrayList<ClientData>();
        for(ClientPojo p : list) {
            list2.add(ClientDto.convert(p));
        }
        return list2;
    }

    @ApiOperation(value = "Update an existing client")
    @RequestMapping(path = "/api/client/{id}", method = RequestMethod.PUT)
    public void update(@PathVariable Long id, @RequestBody ClientForm ClientForm) throws ApiException{
        ClientPojo p = ClientDto.convert(ClientForm);
        clientService.getCheck(id);
        clientService.updateClient(id,p);
    }

}