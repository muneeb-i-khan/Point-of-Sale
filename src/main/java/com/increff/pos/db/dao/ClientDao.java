package com.increff.pos.db.dao;

import com.increff.pos.db.pojo.ClientPojo;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import java.util.List;

@Repository
public class ClientDao {
    private static final String delete_id = "delete from ClientPojo p where id=:id";
    private static final String select_id = "select p from ClientPojo p where id=:id";
    private static final String select_all = "select p from ClientPojo p";
    @PersistenceContext
    EntityManager em;

    public void add(ClientPojo p) {
        em.persist(p);
    }

    public void delete(Long id) {
        Query query = em.createQuery(delete_id);
        query.setParameter("id", id);
        query.executeUpdate();
    }

    public ClientPojo select(Long id) {
        TypedQuery<ClientPojo> query = getQuery(select_id);
        query.setParameter("id", id);
        return query.getSingleResult();
    }

    public List<ClientPojo> selectAll() {
        TypedQuery<ClientPojo> query = getQuery(select_all);
        return query.getResultList();
    }

    public void update(ClientPojo p) {
    }

    TypedQuery<ClientPojo> getQuery(String jpql) {
        return em.createQuery(jpql, ClientPojo.class);
    }
}