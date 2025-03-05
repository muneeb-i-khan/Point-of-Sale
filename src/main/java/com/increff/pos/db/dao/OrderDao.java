package com.increff.pos.db.dao;

import com.increff.pos.db.pojo.OrderPojo;
import com.increff.pos.db.pojo.OrderPojo;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.*;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Optional;

@Repository
@Transactional
public class OrderDao {

    private static final String SELECT_ALL = "SELECT o FROM OrderPojo o";
    private static final String SELECT_BY_ID = "SELECT o FROM OrderPojo o WHERE o.id = :orderId";

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
        try {
            TypedQuery<OrderPojo> query = em.createQuery(SELECT_BY_ID, OrderPojo.class);
            query.setParameter("orderId", id);
            return Optional.ofNullable(query.getSingleResult());
        } catch (NoResultException e) {
            return Optional.empty();
        }
    }

    public List<OrderPojo> selectAllPaginated(int page, int pageSize) {
        TypedQuery<OrderPojo> query = em.createQuery(SELECT_ALL, OrderPojo.class);
        query.setFirstResult(page * pageSize);
        query.setMaxResults(pageSize);
        return query.getResultList();
    }

    public Long countOrders() {
        Query query = em.createQuery("SELECT COUNT(p) FROM OrderPojo p");
        return (Long) query.getSingleResult();
    }

    public int countOrdersByDate(ZonedDateTime date) {
        return ((Number) em.createQuery(
                        "SELECT COUNT(o) FROM OrderPojo o WHERE o.orderDate = :date")
                .setParameter("date", date)
                .getSingleResult()).intValue();
    }

    public int countItemsSoldByDate(ZonedDateTime date) {
        return ((Number) em.createQuery(
                        "SELECT COALESCE(SUM(oi.quantity), 0) FROM OrderItemPojo oi JOIN OrderPojo o ON oi.order_id = o.id WHERE o.orderDate = :date")
                .setParameter("date", date)
                .getSingleResult()).intValue();
    }

    public Double calculateRevenueByDate(ZonedDateTime date) {
        return (Double) em.createQuery(
                        "SELECT COALESCE(SUM(p.price * oi.quantity), 0) " +
                                "FROM OrderItemPojo oi " +
                                "JOIN OrderPojo p ON oi.prod_id = p.id " +
                                "JOIN OrderPojo o ON oi.order_id = o.id " +
                                "WHERE o.orderDate = :date")
                .setParameter("date", date)
                .getSingleResult();
    }


    public void update(OrderPojo order) {
        em.merge(order);
    }
}
