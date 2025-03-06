package com.increff.pos.db.dao;

import com.increff.pos.db.pojo.ClientPojo;
import org.springframework.stereotype.Repository;

import javax.persistence.*;
import java.util.List;

@Repository
public class ClientDao {
    private static final String SELECT_ID = "select p from ClientPojo p where id=:id";
    private static final String SELECT_ALL = "select p from ClientPojo p";
    private static final String SELECT_NAME = "select p from ClientPojo p where name=:name";
    private static final String SELECT_COUNT = "select COUNT(p) from ClientPojo p";

    @PersistenceContext
    EntityManager em;

    public void add(ClientPojo p) {
        em.persist(p);
    }


    public ClientPojo select(Long id) {
        try {
            TypedQuery<ClientPojo> query = getQuery(SELECT_ID);
            query.setParameter("id", id);
            return query.getSingleResult();
        } catch(NoResultException e) {
            return null;
        }
    }

    public List<ClientPojo> selectAll() {
        TypedQuery<ClientPojo> query = getQuery(SELECT_ALL);
        return query.getResultList();
    }

    public void update(ClientPojo p) {
        em.merge(p);
    }

    public ClientPojo selectByName(String name) {
        try {
            TypedQuery<ClientPojo> query = em.createQuery(SELECT_NAME, ClientPojo.class);
            query.setParameter("name", name);
            return query.getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }

    public List<ClientPojo> selectAllPaginated(int page, int pageSize) {
        TypedQuery<ClientPojo> query = em.createQuery(SELECT_ALL, ClientPojo.class);
        query.setFirstResult(page * pageSize);
        query.setMaxResults(pageSize);
        return query.getResultList();
    }

    public Long countClients() {
        Query query = em.createQuery(SELECT_COUNT);
        return (Long) query.getSingleResult();
    }


    TypedQuery<ClientPojo> getQuery(String jpql) {
        return em.createQuery(jpql, ClientPojo.class);
    }
}
