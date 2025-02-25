package com.increff.pos.service;

import com.increff.pos.db.dao.InventoryDao;
import com.increff.pos.db.pojo.InventoryPojo;
import com.increff.pos.db.pojo.ProductPojo;
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
    private ProductService productService;

    public void addInventory(InventoryPojo inventoryPojo) throws ApiException {

        ProductPojo productPojo = productService.getProduct(inventoryPojo.getProd_id());
        InventoryPojo existingInventory = dao.selectByBarcode(productPojo.getBarcode());
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


    public InventoryPojo getInventory(Long id) throws ApiException {
        return getCheck(id);
    }

    public void updateInventory(Long id, InventoryPojo p) throws ApiException {
        InventoryPojo ex = getCheck(id);
        ex.setQuantity(p.getQuantity());
        ex.setProd_id(p.getProd_id());
        dao.update(p);
    }

//    public void deleteInventory(Long id) throws ApiException {
//        InventoryPojo inventoryPojo = getCheck(id);
//        dao.delete(id);
//    }

    public InventoryPojo getCheck(Long id) throws ApiException {
        try {
            InventoryPojo inventoryPojo = dao.select(id);
            if (inventoryPojo == null) {
                throw new ApiException("Inventory with given ID does not exist, id: " + id);
            }
            return inventoryPojo;
        } catch (javax.persistence.NoResultException e) {  // Catch NoResultException
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