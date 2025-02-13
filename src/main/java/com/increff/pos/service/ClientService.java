package com.increff.pos.service;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.increff.pos.db.dao.ClientDao;
import com.increff.pos.db.pojo.ClientPojo;

@Service
public class ClientService {

    @Autowired
    private final ClientDao dao = new ClientDao();

    @Transactional
    public void addClient(ClientPojo p) {
        dao.add(p);
    }

    @Transactional
    public void deleteClient(Long id) {
        dao.delete(id);
    }

    @Transactional(rollbackOn = ApiException.class)
    public ClientPojo getClient(Long id) throws ApiException {
        return getCheck(id);
    }

    @Transactional
    public List<ClientPojo> getAllClients() {
        return dao.selectAll();
    }

    @Transactional(rollbackOn  = ApiException.class)
    public void updateClient(Long id, ClientPojo p) throws ApiException {
        ClientPojo ex = getCheck(id);
        ex.setDescription(p.getDescription());
        ex.setName(p.getName());
        dao.update(p);
    }

    @Transactional
    public ClientPojo getCheck(Long id) throws ApiException {
        ClientPojo p = dao.select(id);
        if (p == null) {
            throw new ApiException("pos with given ID does not exit, id: " + id);
        }
        return p;
    }

}