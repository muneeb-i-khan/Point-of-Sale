package com.increff.pos.db.dao;

import com.increff.pos.db.pojo.OrderPojo;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.*;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Optional;

@Repository
@Transactional
public class OrderDao extends AbstractDao {

    private static final String SELECT_ALL = "SELECT o FROM OrderPojo o";
    private static final String SELECT_BY_ID = "SELECT o FROM OrderPojo o WHERE o.id = :orderId";
    private static final String SELECT_COUNT = "SELECT COUNT(p) FROM OrderPojo p";
    private static final String SELECT_DATE = "SELECT COUNT(o) FROM OrderPojo o WHERE o.orderDate >= :startOfDay AND o.orderDate < :endOfDay";
    private static final String COUNT_ITEMS_SOLD_BY_DATE = "SELECT COALESCE(SUM(oi.quantity), 0) FROM OrderItemPojo oi JOIN OrderPojo o ON oi.orderId = o.id WHERE o.orderDate >= :startOfDay AND o.orderDate < :endOfDay";
    private static final String CALCULATE_REVENUE_BY_DATE = "SELECT COALESCE(SUM(oi.sellingPrice * oi.quantity), 0) \n" +
            "FROM OrderItemPojo oi \n" +
            "JOIN OrderPojo o ON oi.orderId = o.id \n" +
            "WHERE o.orderDate >= :startOfDay AND o.orderDate < :endOfDay\n";


    public void add(OrderPojo p) {
        em.persist(p);
    }

    public List<OrderPojo> selectAll() {
        TypedQuery<OrderPojo> query = getQuery(SELECT_ALL, OrderPojo.class);
        return query.getResultList();
    }

    public Optional<OrderPojo> selectById(Long id) {
        try {
            TypedQuery<OrderPojo> query = getQuery(SELECT_BY_ID, OrderPojo.class);
            query.setParameter("orderId", id);
            return Optional.ofNullable(query.getSingleResult());
        } catch (NoResultException e) {
            return Optional.empty();
        }
    }

    public List<OrderPojo> selectAllPaginated(int page, int pageSize) {
        TypedQuery<OrderPojo> query = getQuery(SELECT_ALL, OrderPojo.class);
        query.setFirstResult(page * pageSize);
        query.setMaxResults(pageSize);
        return query.getResultList();
    }

    public Long countOrders() {
        return (Long) getQuery(SELECT_COUNT, Long.class).getSingleResult();
    }

    public int countOrdersByDate(ZonedDateTime date) {
        ZonedDateTime startOfDay = date.toLocalDate().atStartOfDay(date.getZone());
        ZonedDateTime endOfDay = startOfDay.plusDays(1);  // Start of next day

        return ((Number) getQuery(SELECT_DATE, Number.class)
                .setParameter("startOfDay", startOfDay)
                .setParameter("endOfDay", endOfDay)
                .getSingleResult()).intValue();
    }


    public int countItemsSoldByDate(ZonedDateTime date) {
        ZonedDateTime startOfDay = date.toLocalDate().atStartOfDay(date.getZone());
        ZonedDateTime endOfDay = startOfDay.plusDays(1);

        return ((Number) getQuery(COUNT_ITEMS_SOLD_BY_DATE, Number.class)
                .setParameter("startOfDay", startOfDay)
                .setParameter("endOfDay", endOfDay)
                .getSingleResult()).intValue();
    }


    public Double calculateRevenueByDate(ZonedDateTime date) {
        ZonedDateTime startOfDay = date.toLocalDate().atStartOfDay(date.getZone());
        ZonedDateTime endOfDay = startOfDay.plusDays(1);

        return (Double) getQuery(CALCULATE_REVENUE_BY_DATE, Double.class)
                .setParameter("startOfDay", startOfDay)
                .setParameter("endOfDay", endOfDay)
                .getSingleResult();
    }


    public void update(OrderPojo order) {
    }
}
