package com.increff.pos.db.dao;

import com.increff.pos.db.pojo.ProductPojo;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
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
        TypedQuery<ProductPojo> query = getQuery(select_id);
        query.setParameter("id", id);
        return query.getSingleResult();
    }

    public List<ProductPojo> selectAll() {
        TypedQuery<ProductPojo> query = getQuery(select_all);
        return query.getResultList();
    }

    public void update(ProductPojo p) {
        em.merge(p);
    }

//    public void delete(Long id) {
//        ProductPojo product = em.find(ProductPojo.class, id);
//        if (product != null) {
//            em.remove(product);
//        }
//    }

    public ProductPojo selectByBarcode(String barcode) {
        TypedQuery<ProductPojo> query = em.createQuery(select_by_barcode, ProductPojo.class);
        query.setParameter("barcode", barcode);
        return query.getSingleResult();
    }

    TypedQuery<ProductPojo> getQuery(String jpql) {
        return em.createQuery(jpql, ProductPojo.class);
    }
}

