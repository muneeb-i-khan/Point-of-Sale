package com.increff.pos.db.dao;

import com.increff.pos.db.pojo.SalesReportPojo;
import org.springframework.stereotype.Repository;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import java.time.ZonedDateTime;
import java.util.List;

@Repository
public class SalesReportDao extends AbstractDao {

    private static final String FIND_BY_CLIENT_AND_DATE = "SELECT r FROM SalesReportPojo r WHERE r.clientId = :clientId AND r.date = :date";
    private static final String SELECT_ALL = "SELECT p FROM SalesReportPojo p";
    private static final String COUNT_ALL = "SELECT COUNT(p) FROM SalesReportPojo p";
    private static final String SELECT_FILTER = "SELECT r FROM SalesReportPojo r JOIN ClientPojo c ON r.clientId = c.id WHERE 1=1";

    public void add(SalesReportPojo report) {
        em.persist(report);
    }

    public void update(SalesReportPojo report) {}

    public SalesReportPojo findByClientAndDate(Long clientId, ZonedDateTime date) {
        TypedQuery<SalesReportPojo> query = getQuery(FIND_BY_CLIENT_AND_DATE, SalesReportPojo.class);
        query.setParameter("clientId", clientId);
        query.setParameter("date", date);
        List<SalesReportPojo> result = query.getResultList();
        return result.isEmpty() ? null : result.get(0);
    }

    public List<SalesReportPojo> filterReports(String clientName, String description, ZonedDateTime startDate, ZonedDateTime endDate) {
        StringBuilder queryString = new StringBuilder(SELECT_FILTER);
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

        TypedQuery<SalesReportPojo> query = getQuery(queryString.toString(), SalesReportPojo.class);

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
        return getQuery(SELECT_ALL, SalesReportPojo.class).getResultList();
    }

    public Long countSalesReportPojo() {
        TypedQuery<Long> query = getQuery(COUNT_ALL, Long.class);
        return (Long) query.getSingleResult();
    }

    public List<SalesReportPojo> selectAllPaginated(int page, int pageSize) {
        TypedQuery<SalesReportPojo> query = getQuery(SELECT_ALL, SalesReportPojo.class);
        query.setFirstResult(page * pageSize);
        query.setMaxResults(pageSize);
        return query.getResultList();
    }
}