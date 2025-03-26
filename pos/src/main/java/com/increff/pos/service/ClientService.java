package com.increff.pos.service;

import java.util.List;
import javax.transaction.Transactional;
import com.increff.pos.util.ApiException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.increff.pos.db.dao.ClientDao;
import com.increff.pos.db.pojo.ClientPojo;

@Service
@Transactional(rollbackOn = ApiException.class)
public class ClientService extends AbstractService {

    @Autowired
    private ClientDao dao;

    public void addClient(ClientPojo p) throws ApiException {
        ClientPojo existingClient = dao.selectByName(p.getName());
        isNotNull(existingClient, "Client with name '" + p.getName() + "' already exists.");
        dao.add(p);
    }

    public List<ClientPojo> getAllClients() {
        return dao.selectAll();
    }

    public void updateClient(Long id, ClientPojo p) throws ApiException {
        ClientPojo existingClient = getCheck(id);

        checkDuplicateName(id, p.getName());

        existingClient.setDescription(p.getDescription());
        existingClient.setName(p.getName());

        dao.update(existingClient);
    }

    public List<ClientPojo> getAllClientsPaginated(int page, int pageSize) {
        return dao.selectAllPaginated(page, pageSize);
    }

    public Long getClientCount() {
        return dao.countClients();
    }

    public ClientPojo getCheck(String name) throws ApiException {
        ClientPojo client = dao.selectByName(name);
        isNull(client, "Client with the given name does not exist: " + name);
        return client;
    }

    public ClientPojo getCheck(Long id) throws ApiException {
        ClientPojo p = dao.select(id);
        isNull(p, "Client with given ID does not exist: " + id);
        return p;
    }

    private void checkDuplicateName(Long id, String name) throws ApiException {
        ClientPojo clientWithSameName = dao.selectByName(name);

        if (clientWithSameName != null && !clientWithSameName.getId().equals(id)) {
            throw new ApiException("Name " + name + " already exists");
        }
    }
}