package com.increff.pos.db.dao;

import com.increff.pos.db.pojo.InventoryPojo;
import org.springframework.stereotype.Repository;

import javax.persistence.*;
import java.util.List;

@Repository
public class InventoryDao extends AbstractDao {
    private static final String SELECT_ALL = "SELECT p FROM InventoryPojo p";
    private static final String SELECT_ID = "SELECT p FROM InventoryPojo p WHERE id=:id";
    private static final String SELECT_BARCODE =
            "SELECT i FROM InventoryPojo i JOIN ProductPojo p ON i.prodId = p.id WHERE p.barcode = :barcode";
    private static final String SELECT_COUNT = "SELECT COUNT(p) FROM InventoryPojo p";

    public void add(InventoryPojo p) {
        em.persist(p);
    }

    public List<InventoryPojo> selectAll() {
        return getQuery(SELECT_ALL, InventoryPojo.class).getResultList();
    }

    public InventoryPojo select(Long id) {
        try {
            return getQuery(SELECT_ID, InventoryPojo.class)
                    .setParameter("id", id)
                    .getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }

    public void update(InventoryPojo p) {
    }

    public List<InventoryPojo> selectAllPaginated(int page, int pageSize) {
        TypedQuery<InventoryPojo> query = getQuery(SELECT_ALL, InventoryPojo.class);
        query.setFirstResult(page * pageSize);
        query.setMaxResults(pageSize);
        return query.getResultList();
    }

    public Long countInventories() {
        return getQuery(SELECT_COUNT, Long.class).getSingleResult();
    }


    public InventoryPojo selectByBarcode(String barcode) {
        try {
            return getQuery(SELECT_BARCODE, InventoryPojo.class)
                    .setParameter("barcode", barcode)
                    .getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }
}
