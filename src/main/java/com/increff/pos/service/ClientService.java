package com.increff.pos.service;

import com.increff.pos.db.dao.ClientDao;
import com.increff.pos.db.pojo.ClientPojo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ClientService {
    @Autowired
    private final ClientDao clientDao = new ClientDao();

    public ClientPojo getClient(int id) {
        return clientDao.select(id);
    }

    public List<ClientPojo> getAllClients() {
        return clientDao.selectAll();
    }

    public void addClient(ClientPojo p) {
        clientDao.add(p);
    }

    public void deleteClient(int id) {
        clientDao.delete(id);
    }

    public void updateClient(int id, ClientPojo newP)  {
        clientDao.update(id,newP);
    }
}
