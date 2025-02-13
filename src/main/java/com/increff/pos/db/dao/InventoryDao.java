package com.increff.pos.db.dao;

import com.increff.pos.db.pojo.InventoryPojo;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Repository
public class InventoryDao {
    @PersistenceContext
    EntityManager em;
    public void add(InventoryPojo p) {
        em.persist(p);
    }
}
