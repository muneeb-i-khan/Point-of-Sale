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
    private static final String select_id = "select p from CustomerPojo p where id=:id";
    private static final String select_all = "select p from CustomerPojo p";
    private static final String select_phone = "select p from CustomerPojo p where phone=:phone";
    @PersistenceContext
    EntityManager em;

    public void add(CustomerPojo p) {
        em.persist(p);
    }

    public CustomerPojo select(Long id) {
        try {
            TypedQuery<CustomerPojo> query = getQuery(select_id);
            query.setParameter("id", id);
            return query.getSingleResult();
        } catch(NoResultException e) {
            return null;
        }
    }

    public List<CustomerPojo> selectAll() {
        TypedQuery<CustomerPojo> query = getQuery(select_all);
        return query.getResultList();
    }

    public CustomerPojo selectPhone(String phone) {
        try {
            TypedQuery<CustomerPojo> query = getQuery(select_phone);
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
