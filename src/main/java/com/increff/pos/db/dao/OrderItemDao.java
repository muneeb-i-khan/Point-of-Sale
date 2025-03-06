package com.increff.pos.db.dao;

import com.increff.pos.db.pojo.OrderItemPojo;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import java.util.List;

@Repository
public class OrderItemDao {

    private static final String SELECT_ORDER_ID = "SELECT oi FROM OrderItemPojo oi WHERE oi.orderId = :orderId";

    @PersistenceContext
    EntityManager em;
    public void add(OrderItemPojo p) {
        em.persist(p);
    }

    public List<OrderItemPojo> getItemsByOrderId(Long orderId) {
        TypedQuery<OrderItemPojo> query = em.createQuery(
               SELECT_ORDER_ID , OrderItemPojo.class);
        query.setParameter("orderId", orderId);
        return query.getResultList();
    }

}
