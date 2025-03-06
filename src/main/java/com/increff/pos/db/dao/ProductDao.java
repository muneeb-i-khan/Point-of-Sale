package com.increff.pos.db.dao;

import com.increff.pos.db.pojo.ProductPojo;
import org.springframework.stereotype.Repository;

import javax.persistence.*;
import java.util.List;

@Repository
public class ProductDao {
    private static final String SELECT_ID = "SELECT p FROM ProductPojo p WHERE id=:id";
    private static final String SELECT_ALL = "SELECT p FROM ProductPojo p";
    private static final String SELECT_BARCODE = "SELECT p FROM ProductPojo p WHERE barcode=:barcode";
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
            TypedQuery<ProductPojo> query = getQuery(SELECT_BARCODE);
            query.setParameter("barcode", barcode);
            return query.getSingleResult();
        }
        catch (NoResultException e) {
            return null;
        }
    }

    public List<ProductPojo> selectAllPaginated(int page, int pageSize) {
        TypedQuery<ProductPojo> query = getQuery(SELECT_ALL);
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

