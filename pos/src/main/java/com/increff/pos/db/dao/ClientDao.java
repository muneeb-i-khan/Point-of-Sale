package com.increff.pos.db.dao;

import com.increff.pos.db.pojo.ClientPojo;
import org.springframework.stereotype.Repository;

import javax.persistence.*;
import java.util.List;

@Repository
public class ClientDao extends AbstractDao {
    private static final String SELECT_ID = "SELECT p FROM ClientPojo p WHERE id=:id";
    private static final String SELECT_ALL = "SELECT p FROM ClientPojo p";
    private static final String SELECT_NAME = "SELECT p FROM ClientPojo p WHERE name=:name";
    private static final String SELECT_COUNT = "SELECT COUNT(p) FROM ClientPojo p";

    public void add(ClientPojo p) {
        em.persist(p);
    }

    public ClientPojo select(Long id) {
        try {
            TypedQuery<ClientPojo> query = getQuery(SELECT_ID, ClientPojo.class);
            query.setParameter("id", id);
            return query.getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }

    public List<ClientPojo> selectAll() {
        return getQuery(SELECT_ALL, ClientPojo.class).getResultList();
    }

    public void update(ClientPojo p) {
    }

    public ClientPojo selectByName(String name) {
        try {
            TypedQuery<ClientPojo> query = getQuery(SELECT_NAME, ClientPojo.class);
            query.setParameter("name", name);
            return query.getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }

    public List<ClientPojo> selectAllPaginated(int page, int pageSize) {
        TypedQuery<ClientPojo> query = getQuery(SELECT_ALL, ClientPojo.class);
        query.setFirstResult(page * pageSize);
        query.setMaxResults(pageSize);
        return query.getResultList();
    }

    public Long countClients() {
        return getQuery(SELECT_COUNT, Long.class).getSingleResult();
    }
}
