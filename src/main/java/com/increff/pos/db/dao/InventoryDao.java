package com.increff.pos.db.dao;

import com.increff.pos.db.pojo.InventoryPojo;
import org.springframework.stereotype.Repository;

import javax.persistence.*;
import java.util.List;

@Repository
public class InventoryDao {
    private static final String select_all = "select p from InventoryPojo p";
    private static final String select_id = "select p from InventoryPojo p where id=:id";
    private static final String delete_id = "delete from InventoryPojo p where id=:id";
    private static final String select_by_product_barcode =
            "SELECT i FROM InventoryPojo i JOIN ProductPojo p ON i.prod_id = p.id WHERE p.barcode = :barcode";

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

//    public void delete(Long id) {
//        Query query = em.createQuery(delete_id);
//        query.setParameter("id", id);
//        query.executeUpdate();
//    }

    public InventoryPojo selectByBarcode(String barcode) {
        try {
            TypedQuery<InventoryPojo> query = em.createQuery(select_by_product_barcode, InventoryPojo.class);
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
