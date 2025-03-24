package com.increff.pos.db.dao;

import com.increff.pos.db.pojo.CustomerPojo;
import org.springframework.stereotype.Repository;

import javax.persistence.NoResultException;
import javax.persistence.TypedQuery;
import java.util.List;

@Repository
public class CustomerDao extends AbstractDao {
    private static final String SELECT_ID = "SELECT p FROM CustomerPojo p WHERE id=:id";
    private static final String SELECT_ALL = "SELECT p FROM CustomerPojo p";
    private static final String SELECT_PHONE = "SELECT p FROM CustomerPojo p WHERE phone=:phone";

    public void add(CustomerPojo p) {
        em.persist(p);
    }

    public CustomerPojo select(Long id) {
        try {
            TypedQuery<CustomerPojo> query = getQuery(SELECT_ID, CustomerPojo.class);
            query.setParameter("id", id);
            return query.getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }

    public List<CustomerPojo> selectAll() {
        return getQuery(SELECT_ALL, CustomerPojo.class).getResultList();
    }

    public CustomerPojo selectPhone(String phone) {
        try {
            TypedQuery<CustomerPojo> query = getQuery(SELECT_PHONE, CustomerPojo.class);
            query.setParameter("phone", phone);
            return query.getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }
    public void update(CustomerPojo p) {
    }
}
