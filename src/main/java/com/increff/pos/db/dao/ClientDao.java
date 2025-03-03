package com.increff.pos.db.dao;

import com.increff.pos.db.pojo.ClientPojo;
import org.springframework.stereotype.Repository;

import javax.persistence.*;
import java.util.List;

@Repository
public class ClientDao {
    private static final String select_id = "select p from ClientPojo p where id=:id";
    private static final String select_all = "select p from ClientPojo p";
    private static final String select_by_name = "select p from ClientPojo p where name=:name";

    @PersistenceContext
    EntityManager em;

    public void add(ClientPojo p) {
        em.persist(p);
    }

//    public void delete(Long id) {
//        ClientPojo client = em.find(ClientPojo.class, id);
//        if (client != null) {
//            em.remove(client);
//        }
//    }

    public ClientPojo select(Long id) {
        try {
            TypedQuery<ClientPojo> query = getQuery(select_id);
            query.setParameter("id", id);
            return query.getSingleResult();
        } catch(NoResultException e) {
            return null;
        }
    }

    public List<ClientPojo> selectAll() {
        TypedQuery<ClientPojo> query = getQuery(select_all);
        return query.getResultList();
    }

    public void update(ClientPojo p) {
        em.merge(p);
    }

    public ClientPojo selectByName(String name) {
        TypedQuery<ClientPojo> query = em.createQuery(select_by_name, ClientPojo.class);
        query.setParameter("name", name);
        return query.getSingleResult();
    }

    public List<ClientPojo> selectAllPaginated(int page, int pageSize) {
        TypedQuery<ClientPojo> query = em.createQuery(select_all, ClientPojo.class);
        query.setFirstResult(page * pageSize);
        query.setMaxResults(pageSize);
        return query.getResultList();
    }

    public Long countClients() {
        Query query = em.createQuery("SELECT COUNT(p) FROM ClientPojo p");
        return (Long) query.getSingleResult();
    }


    TypedQuery<ClientPojo> getQuery(String jpql) {
        return em.createQuery(jpql, ClientPojo.class);
    }
}
