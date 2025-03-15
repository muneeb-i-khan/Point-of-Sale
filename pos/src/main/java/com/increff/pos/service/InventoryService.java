package com.increff.pos.service;

import com.increff.pos.db.dao.InventoryDao;
import com.increff.pos.db.pojo.InventoryPojo;
import com.increff.pos.db.pojo.ProductPojo;
import com.increff.pos.flow.InventoryFlow;
import com.increff.pos.util.ApiException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
@Transactional(rollbackOn = ApiException.class)
public class InventoryService {
    @Autowired
    private InventoryDao dao;

    @Autowired
    private InventoryFlow inventoryFlow;

    public void addInventory(InventoryPojo inventoryPojo) throws ApiException {
        if (inventoryPojo.getQuantity() < 0) throw new ApiException("Quantity can't be negative");
        String prodBarcode = inventoryFlow.getBarcode(inventoryPojo);
        InventoryPojo existingInventory = dao.selectByBarcode(prodBarcode);
        if (existingInventory != null) {
            existingInventory.setQuantity(existingInventory.getQuantity() + inventoryPojo.getQuantity());
            dao.update(existingInventory);
        } else {
            dao.add(inventoryPojo);
        }
    }

    public List<InventoryPojo> getAllInventories() {
        return dao.selectAll();
    }

    public List<InventoryPojo> getAllInventoriesPaginated(int page, int pageSize) {
        return dao.selectAllPaginated(page, pageSize);
    }

    public Long getInventoryCount() {
        return dao.countInventories();
    }

    public void updateInventory(Long id, InventoryPojo p) throws ApiException {
        InventoryPojo ex = getCheck(id);
        ex.setQuantity(p.getQuantity());
        ex.setProdId(p.getProdId());
        dao.update(ex);
    }

    public InventoryPojo getCheck(Long id) throws ApiException {
        try {
            InventoryPojo inventoryPojo = dao.select(id);
            if (inventoryPojo == null) {
                throw new ApiException("Inventory with given ID does not exist, id: " + id);
            }
            return inventoryPojo;
        } catch (javax.persistence.NoResultException e) {
            throw new ApiException("Inventory with given ID does not exist, id: " + id);
        }
    }

    public InventoryPojo getInventoryByBarcode(String barcode) throws ApiException {
        InventoryPojo inventory = dao.selectByBarcode(barcode);
        if (inventory == null) {
            throw new ApiException("Inventory not found for barcode: " + barcode);
        }
        return inventory;
    }

}