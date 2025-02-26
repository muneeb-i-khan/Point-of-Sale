package com.increff.pos.controller;

import com.increff.pos.dto.ClientDto;
import com.increff.pos.model.data.ClientData;
import com.increff.pos.model.forms.ClientForm;
import com.increff.pos.service.ApiException;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Collections;
import java.util.List;
import java.util.Map;

@Api
@RestController
@RequestMapping("/api/client")
public class ClientController {

    @Autowired
    private ClientDto clientDto;

    @PostMapping
    public ResponseEntity<Map<String, String>> addClient(@Valid @RequestBody ClientForm clientForm) {
        clientDto.addClient(clientForm);
        return ResponseEntity.ok(Collections.singletonMap("message", "Client added successfully"));
    }


    @ApiOperation(value = "Get a client based on its Id")
    @GetMapping("/{id}")
    public ResponseEntity<ClientData> get(@PathVariable Long id) throws ApiException {
        ClientData clientData = clientDto.getClient(id);
        return ResponseEntity.ok(clientData);
    }

    @ApiOperation(value = "Get all clients")
    @GetMapping
    public ResponseEntity<List<ClientData>> getAll() {
        List<ClientData> clients = clientDto.getAllClients();
        return ResponseEntity.ok(clients);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Map<String, String>> update(@PathVariable Long id, @RequestBody ClientForm clientForm) throws ApiException {
        clientDto.updateClient(clientForm, id);
        return ResponseEntity.ok(Collections.singletonMap("message", "Client updated successfully"));
    }
}
