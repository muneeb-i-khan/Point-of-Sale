package com.increff.pos.db.dao;

import com.increff.pos.db.pojo.OrderPojo;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import java.util.List;
import java.util.Optional;

@Repository
public class OrderDao {

    private static final String SELECT_ALL = "SELECT DISTINCT o FROM OrderPojo o LEFT JOIN FETCH o.salesItems";
    private static final String SELECT_BY_ID = "SELECT DISTINCT o FROM OrderPojo o LEFT JOIN FETCH o.salesItems WHERE o.id = :orderId";
    private static final String DELETE_BY_ID = "DELETE FROM OrderPojo o WHERE o.id = :id";

    @PersistenceContext
    private EntityManager em;

    public void add(OrderPojo p) {
        em.persist(p);
    }

    public List<OrderPojo> selectAll() {
        TypedQuery<OrderPojo> query = em.createQuery(SELECT_ALL, OrderPojo.class);
        return query.getResultList();
    }

    public Optional<OrderPojo> selectById(Long id) {
        TypedQuery<OrderPojo> query = em.createQuery(SELECT_BY_ID, OrderPojo.class);
        query.setParameter("orderId", id);
        List<OrderPojo> resultList = query.getResultList();
        return resultList.isEmpty() ? Optional.empty() : Optional.of(resultList.get(0));
    }

    @Transactional
    public void delete(Long id) {
        selectById(id).ifPresent(order -> em.remove(order));
    }

    public void update(OrderPojo order) {
        em.merge(order);
    }
}
