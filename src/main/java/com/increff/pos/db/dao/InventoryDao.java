package com.increff.pos.db.dao;

import com.increff.pos.db.pojo.InventoryPojo;
import org.springframework.stereotype.Repository;

import javax.persistence.*;
import java.util.List;

@Repository
public class InventoryDao {
    private static final String SELECT_ALL = "select p from InventoryPojo p";
    private static final String SELECT_ID = "select p from InventoryPojo p where id=:id";
    private static final String SELECT_BARCODE =
            "SELECT i FROM InventoryPojo i JOIN ProductPojo p ON i.prod_id = p.id WHERE p.barcode = :barcode";
    private static final String SELECT_COUNT = "SELECT COUNT(p) FROM InventoryPojo p";

    @PersistenceContext
    EntityManager em;
    public void add(InventoryPojo p) {
        em.persist(p);
    }

    public List<InventoryPojo> selectAll() {
        TypedQuery<InventoryPojo> query = getQuery(SELECT_ALL);
        return query.getResultList();
    }

    public InventoryPojo select(Long id) {
        TypedQuery<InventoryPojo> query = getQuery(SELECT_ID);
        query.setParameter("id",id);
        return query.getSingleResult();
    }

    public void update(InventoryPojo p) {
    }


    public List<InventoryPojo> selectAllPaginated(int page, int pageSize) {
        TypedQuery<InventoryPojo> query = em.createQuery(SELECT_ALL, InventoryPojo.class);
        query.setFirstResult(page * pageSize);
        query.setMaxResults(pageSize);
        return query.getResultList();
    }

    public Long countInventories() {
        Query query = em.createQuery(SELECT_COUNT);
        return (Long) query.getSingleResult();
    }
    
    public InventoryPojo selectByBarcode(String barcode) {
        try {
            TypedQuery<InventoryPojo> query = em.createQuery(SELECT_BARCODE, InventoryPojo.class);
            query.setParameter("barcode", barcode);
            return query.getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }

    TypedQuery<InventoryPojo> getQuery(String jpql) {
        return em.createQuery(jpql, InventoryPojo.class);
    }
}
