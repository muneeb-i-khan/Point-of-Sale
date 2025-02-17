package com.increff.pos.controller;

import com.increff.pos.dto.ClientDto;
import com.increff.pos.model.ClientData;
import com.increff.pos.model.ClientForm;
import com.increff.pos.service.ApiException;
import com.increff.pos.service.ClientService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@Api
@RestController
public class ClientController {
    @Autowired
    ClientDto clientDto;

    @ApiOperation(value = "Post a client")
    @RequestMapping(path="/api/client/", method = RequestMethod.POST)
    public void addClient(@RequestBody ClientForm clientForm) {
        clientDto.addClient(clientForm);
    }

    @ApiOperation(value = "Delete a client")
    @RequestMapping(path="/api/client/{id}", method = RequestMethod.DELETE)
    public void delete(@PathVariable Long id) throws ApiException  {
        clientDto.deleteClient(id);
    }

    @ApiOperation(value = "Get a client based on it's Id")
    @RequestMapping(path = "/api/client/{id}", method = RequestMethod.GET)
    public ClientData get(@PathVariable Long id) throws ApiException {
        return clientDto.getClient(id);
    }

    @ApiOperation(value = "Get all clients")
    @RequestMapping(path = "/api/client", method = RequestMethod.GET)
    public List<ClientData> getAll() {
        return clientDto.getAllClients();
    }

    @ApiOperation(value = "Update an existing client")
    @RequestMapping(path = "/api/client/{id}", method = RequestMethod.PUT)
    public void update(@PathVariable Long id, @RequestBody ClientForm clientForm) throws ApiException{
        clientDto.updateClient(clientForm, id);
    }

}