package com.increff.pos.dto;

import static org.junit.Assert.*;

import com.increff.pos.service.AbstractUnitTest;
import com.increff.pos.service.QaConfig;
import com.increff.pos.model.data.ClientData;
import com.increff.pos.model.forms.ClientForm;
import com.increff.pos.util.ApiException;
import com.increff.pos.service.ClientService;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@ContextConfiguration(classes = QaConfig.class)
@Transactional
@Rollback
public class ClientDtoTest extends AbstractUnitTest {

    @Autowired
    private ClientDto clientDto;

    @Autowired
    private ClientService clientService;

    @Test
    public void addClientTest() {
        ClientForm clientForm = new ClientForm();
        clientForm.setName("Tesla");
        clientForm.setDescription("Electric Cars");

        clientDto.addClient(clientForm);

        List<ClientData> clients = clientDto.getAllClients();
        assertEquals(1, clients.size());
        assertEquals("tesla", clients.get(0).getName());
        assertEquals("Electric Cars", clients.get(0).getDescription());
    }

    @Test
    public void getClientTest() throws ApiException {
        ClientForm clientForm = new ClientForm();
        clientForm.setName("Sony");
        clientForm.setDescription("Electronics");

        clientDto.addClient(clientForm);
        List<ClientData> clients = clientDto.getAllClients();
        ClientData clientData = clientDto.getClient(clients.get(0).getId());

        assertNotNull(clientData);
        assertEquals("sony", clientData.getName());
        assertEquals("Electronics", clientData.getDescription());
    }

    @Test(expected = ApiException.class)
    public void getClientNotFoundTest() throws ApiException {
        clientDto.getClient(999L);
    }

    @Test
    public void updateClientTest() throws ApiException {
        ClientForm clientForm = new ClientForm();
        clientForm.setName("Apple");
        clientForm.setDescription("Technology");

        clientDto.addClient(clientForm);
        List<ClientData> clients = clientDto.getAllClients();
        Long clientId = clients.get(0).getId();

        ClientForm updateForm = new ClientForm();
        updateForm.setName("Apple Inc.");
        updateForm.setDescription("Updated Tech Company");

        clientDto.updateClient(updateForm, clientId);

        ClientData updatedClient = clientDto.getClient(clientId);
        assertEquals("apple inc.", updatedClient.getName());
        assertEquals("Updated Tech Company", updatedClient.getDescription());
    }

    @Test(expected = ApiException.class)
    public void updateClientNotFoundTest() throws ApiException {
        ClientForm updateForm = new ClientForm();
        updateForm.setName("NonExistent");
        updateForm.setDescription("No Update");

        clientDto.updateClient(updateForm, 999L);
    }
}
