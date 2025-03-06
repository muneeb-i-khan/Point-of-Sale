package com.increff.pos.db.dao;

import com.increff.pos.db.pojo.ProductPojo;
import org.springframework.stereotype.Repository;

import javax.persistence.*;
import java.util.List;

@Repository
public class ProductDao {
    private static final String SELECT_ID = "select p from ProductPojo p where id=:id";
    private static final String SELECT_ALL = "select p from ProductPojo p";
    private static final String SELECT_BARCODE = "select p from ProductPojo p where barcode=:barcode";
    private static final String SELECT_COUNT = "SELECT COUNT(p) FROM ProductPojo p";

    @PersistenceContext
    EntityManager em;

    public void add(ProductPojo p) {
        em.persist(p);
    }

    public ProductPojo select(Long id) {
        try {
            TypedQuery<ProductPojo> query = getQuery(SELECT_ID);
            query.setParameter("id", id);
            return query.getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }


    public List<ProductPojo> selectAll() {
        TypedQuery<ProductPojo> query = getQuery(SELECT_ALL);
        return query.getResultList();
    }

    public void update(ProductPojo p) {
        em.merge(p);
    }

    public ProductPojo selectByBarcode(String barcode) {
        try {
            TypedQuery<ProductPojo> query = em.createQuery(SELECT_BARCODE, ProductPojo.class);
            query.setParameter("barcode", barcode);
            return query.getSingleResult();
        }
        catch (NoResultException e) {
            return null;
        }
    }

    public List<ProductPojo> selectAllPaginated(int page, int pageSize) {
        TypedQuery<ProductPojo> query = em.createQuery(SELECT_ALL, ProductPojo.class);
        query.setFirstResult(page * pageSize);
        query.setMaxResults(pageSize);
        return query.getResultList();
    }

    public Long countProducts() {
        Query query = em.createQuery(SELECT_COUNT);
        return (Long) query.getSingleResult();
    }


    TypedQuery<ProductPojo> getQuery(String jpql) {
        return em.createQuery(jpql, ProductPojo.class);
    }
}

