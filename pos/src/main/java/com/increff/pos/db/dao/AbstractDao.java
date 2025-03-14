package com.increff.pos.db.dao;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

public abstract class AbstractDao {

    @PersistenceContext
    protected EntityManager em;

    public <T> TypedQuery<T> getQuery(String jpql, Class<T> tclass) {
        return em.createQuery(jpql, tclass);
    }
}
