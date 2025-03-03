package com.increff.pos.db.dao;

import com.increff.pos.db.pojo.DaySaleReportPojo;
import com.increff.pos.db.pojo.SalesReportPojo;
import org.springframework.stereotype.Repository;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import java.time.LocalDate;
import java.util.List;

@Repository
public class SalesReportDao {

    @PersistenceContext
    private EntityManager em;

    public void add(SalesReportPojo report) {
        em.persist(report);
    }

    public void update(SalesReportPojo report) {
        em.merge(report);
    }

    public SalesReportPojo findByClientAndDate(Long clientId, LocalDate date) {
        TypedQuery<SalesReportPojo> query = em.createQuery(
                "SELECT r FROM SalesReportPojo r WHERE r.clientId = :clientId AND r.date = :date",
                SalesReportPojo.class
        );
        query.setParameter("clientId", clientId);
        query.setParameter("date", date);
        List<SalesReportPojo> result = query.getResultList();
        return result.isEmpty() ? null : result.get(0);
    }

    public List<SalesReportPojo> filterReports(String clientName, String description, LocalDate startDate, LocalDate endDate) {
        StringBuilder queryString = new StringBuilder("SELECT r FROM SalesReportPojo r JOIN ClientPojo c ON r.clientId = c.id WHERE 1=1");
        if (clientName != null && !clientName.isEmpty()) {
            queryString.append(" AND c.name LIKE :clientName");
        }
        if (description != null && !description.isEmpty()) {
            queryString.append(" AND c.description LIKE :description");
        }
        if (startDate != null) {
            queryString.append(" AND r.date >= :startDate");
        }
        if (endDate != null) {
            queryString.append(" AND r.date <= :endDate");
        }

        TypedQuery<SalesReportPojo> query = em.createQuery(queryString.toString(), SalesReportPojo.class);

        if (clientName != null && !clientName.isEmpty()) {
            query.setParameter("clientName", "%" + clientName + "%");
        }
        if (description != null && !description.isEmpty()) {
            query.setParameter("description", "%" + description + "%");
        }
        if (startDate != null) {
            query.setParameter("startDate", startDate);
        }
        if (endDate != null) {
            query.setParameter("endDate", endDate);
        }

        return query.getResultList();
    }

    public List<SalesReportPojo> selectAll() {
        return em.createQuery("SELECT p FROM SalesReportPojo p", SalesReportPojo.class)
                .getResultList();
    }

    public Long countSalesReportPojo() {
        Query query = em.createQuery("SELECT COUNT(p) FROM SalesReportPojo p");
        return (Long) query.getSingleResult();
    }

    public List<SalesReportPojo> selectAllPaginated(int page, int pageSize) {
        TypedQuery<SalesReportPojo> query = em.createQuery("SELECT p FROM SalesReportPojo p", SalesReportPojo.class);
        query.setFirstResult(page * pageSize);
        query.setMaxResults(pageSize);
        return query.getResultList();
    }
}
