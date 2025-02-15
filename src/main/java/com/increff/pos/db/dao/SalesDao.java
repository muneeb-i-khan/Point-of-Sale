package com.increff.pos.db.dao;

import com.increff.pos.db.pojo.SalesPojo;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Repository
public class SalesDao {

    @PersistenceContext
    EntityManager em;
    public void add(SalesPojo p) {
        em.persist(p);
    }
}
