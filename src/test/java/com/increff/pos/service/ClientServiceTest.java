package com.increff.pos.service;

import static org.junit.Assert.*;

import com.increff.pos.db.pojo.ClientPojo;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = QaConfig.class)
@Transactional
@Rollback
public class ClientServiceTest extends AbstractUnitTest {

    @Autowired
    private ClientService clientService;

    @Test
    public void addClientTest() throws ApiException {
        ClientPojo p = new ClientPojo();
        p.setDescription("Car");
        p.setName("Honda");

        clientService.addClient(p);

        ClientPojo retrievedClient = clientService.getClientByName("Honda");
        assertNotNull(retrievedClient);
        assertEquals("Honda", retrievedClient.getName());
        assertEquals("Car", retrievedClient.getDescription());
    }

    @Test
    public void getClientByIdTest() throws ApiException {
        ClientPojo p = new ClientPojo();
        p.setDescription("Electronics");
        p.setName("Samsung");

        clientService.addClient(p);

        ClientPojo savedClient = clientService.getClientByName("Samsung");
        ClientPojo retrievedClient = clientService.getClient(savedClient.getId());

        assertNotNull(retrievedClient);
        assertEquals("Samsung", retrievedClient.getName());
        assertEquals("Electronics", retrievedClient.getDescription());
    }

    @Test(expected = ApiException.class)
    public void getClientByIdNotFoundTest() throws ApiException {
        clientService.getClient(999L); // ID that doesn't exist
    }

    @Test
    public void getAllClientsTest() throws ApiException {
        ClientPojo p1 = new ClientPojo();
        p1.setDescription("Car");
        p1.setName("Honda");

        ClientPojo p2 = new ClientPojo();
        p2.setDescription("Electronics");
        p2.setName("Samsung");

        clientService.addClient(p1);
        clientService.addClient(p2);

        List<ClientPojo> clients = clientService.getAllClients();
        assertEquals(2, clients.size());
    }

    @Test
    public void updateClientTest() throws ApiException {
        ClientPojo p = new ClientPojo();
        p.setDescription("Mobile");
        p.setName("Apple");

        clientService.addClient(p);
        ClientPojo savedClient = clientService.getClientByName("Apple");

        ClientPojo updatePojo = new ClientPojo();
        updatePojo.setName("Apple Inc.");
        updatePojo.setDescription("Updated Mobile Company");

        clientService.updateClient(savedClient.getId(), updatePojo);

        ClientPojo updatedClient = clientService.getClient(savedClient.getId());
        assertEquals("Apple Inc.", updatedClient.getName());
        assertEquals("Updated Mobile Company", updatedClient.getDescription());
    }

    @Test(expected = ApiException.class)
    public void updateClientNotFoundTest() throws ApiException {
        ClientPojo updatePojo = new ClientPojo();
        updatePojo.setName("NonExistent");
        updatePojo.setDescription("No Update");

        clientService.updateClient(999L, updatePojo);
    }

//    @Test
//    public void deleteClientNotFoundTest() {
//        try {
//            clientService.deleteClient(3L);
//        } catch (ApiException e) {
//            assertEquals("Client with given ID does not exist: 3", e.getMessage());
//        }
//    }

//    @Test(expected = ApiException.class)
//    public void deleteClientTest() throws ApiException {
//        ClientPojo p = new ClientPojo();
//        p.setDescription("Appliances");
//        p.setName("LG");
//        clientService.addClient(p);
//        ClientPojo savedClient = clientService.getClientByName("LG");
//        clientService.deleteClient(savedClient.getId());
//        clientService.getClient(savedClient.getId());
//    }

}
