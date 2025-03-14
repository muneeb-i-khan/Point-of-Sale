package com.increff.pos.controller;

import com.increff.pos.dto.ClientDto;
import com.increff.pos.model.data.ClientData;
import com.increff.pos.model.forms.ClientForm;
import com.increff.pos.util.ApiException;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.List;

@Api
@RestController
@RequestMapping("/api/client")
public class ClientController {

    @Autowired
    private ClientDto clientDto;

    @ApiOperation(value = "Add a client")
    @PostMapping
    public ClientData addClient(@Valid @RequestBody ClientForm clientForm) {
        return clientDto.addClient(clientForm);
    }


    @ApiOperation(value = "Get a client based on its Id")
    @GetMapping("/{id}")
    public ClientData get(@PathVariable Long id) throws ApiException {
        return clientDto.getClient(id);
    }

    @ApiOperation(value = "Get all clients with pagination")
    @GetMapping("/paginated")
    public List<ClientData> getAllPaginated(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "15") int pageSize, HttpServletResponse httpServletResponse) {
        return clientDto.getAllClientsPaginated(page, pageSize, httpServletResponse);
    }


    @PutMapping("/{id}")
    public ClientData update(@PathVariable Long id, @Valid @RequestBody ClientForm clientForm) throws ApiException {
        return clientDto.updateClient(clientForm, id);
    }
}
