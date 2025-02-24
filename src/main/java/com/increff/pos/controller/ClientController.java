package com.increff.pos.controller;

import com.increff.pos.dto.ClientDto;
import com.increff.pos.model.data.ClientData;
import com.increff.pos.model.forms.ClientForm;
import com.increff.pos.service.ApiException;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@Api
@RestController
@RequestMapping("/api/client")
public class ClientController {
    @Autowired
    ClientDto clientDto;

    @ApiOperation(value = "Post a client")
    @PostMapping
    public void addClient(@RequestBody ClientForm clientForm) {
        clientDto.addClient(clientForm);
    }

//    @ApiOperation(value = "Delete a client")
//    @DeleteMapping("/{id}")
//    public void delete(@PathVariable Long id) throws ApiException {
//        clientDto.deleteClient(id);
//    }

    @ApiOperation(value = "Get a client based on its Id")
    @GetMapping("/{id}")
    public ClientData get(@PathVariable Long id) throws ApiException {
        return clientDto.getClient(id);
    }

    @ApiOperation(value = "Get all clients")
    @GetMapping
    public List<ClientData> getAll() {
        return clientDto.getAllClients();
    }

    @ApiOperation(value = "Update an existing client")
    @PutMapping("/{id}")
    public void update(@PathVariable Long id, @RequestBody ClientForm clientForm) throws ApiException {
        clientDto.updateClient(clientForm, id);
    }
}
