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
public class ClientService {

    @Autowired
    private  ClientDao dao;

    public void addClient(ClientPojo p) throws ApiException {
        ClientPojo existingClient = dao.selectByName(p.getName());
        if (existingClient != null) {
            throw new ApiException("Client with name '" + p.getName() + "' already exists.");
        }
        dao.add(p);
    }



    public ClientPojo getClient(Long id) throws ApiException {
        return getCheck(id);
    }

    public List<ClientPojo> getAllClients() {
        return dao.selectAll();
    }


    public void updateClient(Long id, ClientPojo p) throws ApiException {
        ClientPojo ex = getCheck(id);
        ex.setDescription(p.getDescription());
        ex.setName(p.getName());
        dao.update(ex);
    }

    public List<ClientPojo> getAllClientsPaginated(int page, int pageSize) {
        return dao.selectAllPaginated(page, pageSize);
    }

    public Long getClientCount() {
        return dao.countClients();
    }


    public ClientPojo getClientByName(String name) throws ApiException {
        ClientPojo client = dao.selectByName(name);
        if (client == null) {
            throw new ApiException("Client with the given name does not exist: " + name);
        }
        return client;
    }

    public ClientPojo getCheck(Long id) throws ApiException {
        ClientPojo p = dao.select(id);
        if (p == null) {
            throw new ApiException("Client with given ID does not exist: " + id);
        }
        return p;
    }
}
