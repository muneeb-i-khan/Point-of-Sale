package com.increff.pos.service;

import com.increff.pos.db.dao.InventoryDao;
import com.increff.pos.db.pojo.InventoryPojo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
public class InventoryService {
    @Autowired
    private final InventoryDao dao = new InventoryDao();

    @Transactional
    public void addInventory(InventoryPojo p) {
        dao.add(p);
    }
    @Transactional
    public List<InventoryPojo> getAllInventories() {
        return dao.selectAll();
    }

    @Transactional(rollbackOn = ApiException.class)
    public InventoryPojo getInventory(Long id) throws ApiException {
        return getCheck(id);
    }

    @Transactional(rollbackOn  = ApiException.class)
    public void updateInventory(Long id, InventoryPojo p) throws ApiException {
        InventoryPojo ex = getCheck(id);
        ex.setQuantity(p.getQuantity());
        ex.setBarcode(p.getBarcode());
        ex.setProductPojo(p.getProductPojo());
        dao.update(p);
    }

    @Transactional
    public void deleteInventory(Long id) {
        dao.delete(id);
    }

    @Transactional
    public InventoryPojo getCheck(Long id) throws ApiException {
        InventoryPojo inventoryPojo = dao.select(id);
        if (inventoryPojo == null) {
            throw new ApiException("Inventory with given ID does not exit, id: " + id);
        }
        return inventoryPojo;
    }

    @Transactional(rollbackOn = ApiException.class)
    public InventoryPojo getInventoryByBarcode(String barcode) throws ApiException {
        InventoryPojo inventory = dao.selectByBarcode(barcode);
        if (inventory == null) {
            throw new ApiException("Inventory not found for barcode: " + barcode);
        }
        return inventory;
    }
}