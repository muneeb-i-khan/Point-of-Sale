package com.increff.db.dao;

import com.increff.db.pojo.InvoicePojo;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import java.util.List;

@Repository
public class InvoiceDao {
    @PersistenceContext
    EntityManager em;

    private static final String SELECT_ALL = "select p from InvoicePojo p";
    private static final String SELECT_BY_ORDER_ID = "select p from InvoicePojo p where orderId=:orderId";

    public void add(InvoicePojo p) {
        em.persist(p);
    }

    public InvoicePojo select(Long id) {
        try {
            TypedQuery<InvoicePojo> query = getQuery(SELECT_BY_ORDER_ID);
            query.setParameter("orderId", id);
            return query.getSingleResult();
        } catch(NoResultException e) {
            return null;
        }
    }

    public List<InvoicePojo> selectAll() {
        TypedQuery<InvoicePojo> query = getQuery(SELECT_ALL);
        return query.getResultList();
    }

    public void update(InvoicePojo p) {
        em.merge(p);
    }

    TypedQuery<InvoicePojo> getQuery(String jpql) {
        return em.createQuery(jpql, InvoicePojo.class);
    }

}
