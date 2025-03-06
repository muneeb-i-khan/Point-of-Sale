package com.increff.pos.db.dao;

import com.increff.pos.db.pojo.CustomerPojo;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import java.util.List;

@Repository
public class CustomerDao {
    private static final String SELECT_ID = "SELECT p FROM CustomerPojo p WHERE id=:id";
    private static final String SELECT_ALL = "SELECT p FROM CustomerPojo p";
    private static final String SELECT_PHONE = "SELECT p FROM CustomerPojo p WHERE phone=:phone";
    @PersistenceContext
    EntityManager em;

    public void add(CustomerPojo p) {
        em.persist(p);
    }

    public CustomerPojo select(Long id) {
        try {
            TypedQuery<CustomerPojo> query = getQuery(SELECT_ID);
            query.setParameter("id", id);
            return query.getSingleResult();
        } catch(NoResultException e) {
            return null;
        }
    }

    public List<CustomerPojo> selectAll() {
        TypedQuery<CustomerPojo> query = getQuery(SELECT_ALL);
        return query.getResultList();
    }

    public CustomerPojo selectPhone(String phone) {
        try {
            TypedQuery<CustomerPojo> query = getQuery(SELECT_PHONE);
            query.setParameter("phone", phone);
            return query.getSingleResult();
        } catch(NoResultException e) {
            return null;
        }
    }

    public void update(CustomerPojo p) {
        em.merge(p);
    }


    TypedQuery<CustomerPojo> getQuery(String jpql) {
        return em.createQuery(jpql, CustomerPojo.class);
    }
}
