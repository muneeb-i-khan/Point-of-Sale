package com.increff.pos.db.dao;

import com.increff.pos.db.pojo.OrderPojo;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Repository
public class OrderDao {

    @PersistenceContext
    EntityManager em;
    public void add(OrderPojo p) {
        em.persist(p);
    }
}
