package com.increff.pos.db.dao;

import com.increff.pos.db.pojo.ProductPojo;
import com.increff.pos.db.pojo.ProductPojo;
import org.springframework.stereotype.Repository;

import javax.persistence.*;
import java.util.List;

@Repository
public class ProductDao {
    private static final String select_id = "select p from ProductPojo p where id=:id";
    private static final String select_all = "select p from ProductPojo p";
    private static final String select_by_barcode = "select p from ProductPojo p where barcode=:barcode";

    @PersistenceContext
    EntityManager em;

    public void add(ProductPojo p) {
        em.persist(p);
    }

    public ProductPojo select(Long id) {
        try {
            TypedQuery<ProductPojo> query = getQuery(select_id);
            query.setParameter("id", id);
            return query.getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }


    public List<ProductPojo> selectAll() {
        TypedQuery<ProductPojo> query = getQuery(select_all);
        return query.getResultList();
    }

    public void update(ProductPojo p) {
        em.merge(p);
    }

    public ProductPojo selectByBarcode(String barcode) {
        TypedQuery<ProductPojo> query = em.createQuery(select_by_barcode, ProductPojo.class);
        query.setParameter("barcode", barcode);
        List<ProductPojo> results = query.getResultList();
        return results.isEmpty() ? null : results.get(0);
    }

    public List<ProductPojo> selectAllPaginated(int page, int pageSize) {
        TypedQuery<ProductPojo> query = em.createQuery(select_all, ProductPojo.class);
        query.setFirstResult(page * pageSize);
        query.setMaxResults(pageSize);
        return query.getResultList();
    }

    public Long countProducts() {
        Query query = em.createQuery("SELECT COUNT(p) FROM ProductPojo p");
        return (Long) query.getSingleResult();
    }


    TypedQuery<ProductPojo> getQuery(String jpql) {
        return em.createQuery(jpql, ProductPojo.class);
    }
}

