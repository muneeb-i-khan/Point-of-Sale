package com.increff.pos.db.dao;

import com.increff.pos.db.pojo.InventoryPojo;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import java.util.List;

@Repository
public class InventoryDao {
    private static final String select_all = "select p from InventoryPojo p";
    private static final String select_id = "select p from InventoryPojo p where id=:id";
    private static final String delete_id = "delete from InventoryPojo p where id=:id";

    @PersistenceContext
    EntityManager em;
    public void add(InventoryPojo p) {
        em.persist(p);
    }

    public List<InventoryPojo> selectAll() {
        TypedQuery<InventoryPojo> query = getQuery(select_all);
        return query.getResultList();
    }

    public InventoryPojo select(Long id) {
        TypedQuery<InventoryPojo> query = getQuery(select_id);
        query.setParameter("id",id);
        return query.getSingleResult();
    }

    public void update(InventoryPojo p) {
    }

    public void delete(Long id) {
        Query query = em.createQuery(delete_id);
        query.setParameter("id", id);
        query.executeUpdate();
    }

    TypedQuery<InventoryPojo> getQuery(String jpql) {
        return em.createQuery(jpql, InventoryPojo.class);
    }
}
