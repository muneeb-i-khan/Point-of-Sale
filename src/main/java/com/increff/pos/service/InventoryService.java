package com.increff.pos.service;

import com.increff.pos.db.dao.InventoryDao;
import com.increff.pos.db.pojo.InventoryPojo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
@Service
public class InventoryService {
    @Autowired
    private final InventoryDao dao = new InventoryDao();

    @Transactional
    public void addInventory(InventoryPojo p) {
        dao.add(p);
    }
}
