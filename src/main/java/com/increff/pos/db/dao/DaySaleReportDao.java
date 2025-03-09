package com.increff.pos.db.dao;

import com.increff.pos.db.pojo.DaySaleReportPojo;
import org.springframework.stereotype.Repository;

import javax.persistence.Query;
import javax.persistence.TypedQuery;
import java.time.ZonedDateTime;
import java.util.List;

@Repository
public class DaySaleReportDao extends AbstractDao {
    private static final String SELECT_ALL = "SELECT p FROM DaySaleReportPojo p";
    private static final String SELECT_COUNT = "SELECT COUNT(p) FROM DaySaleReportPojo p";
    private static final String SELECT_DATE_FILTER = "SELECT p FROM DaySaleReportPojo p WHERE p.date BETWEEN :start AND :end";
    private static final String SELECT_DATE = "SELECT p FROM DaySaleReportPojo p WHERE p.date = :date";

    public void save(DaySaleReportPojo sale) {
        em.persist(sale);
    }

    public List<DaySaleReportPojo> selectAll() {
        return getQuery(SELECT_ALL, DaySaleReportPojo.class).getResultList();
    }

    public List<DaySaleReportPojo> selectAllPaginated(int page, int pageSize) {
        TypedQuery<DaySaleReportPojo> query = getQuery(SELECT_ALL, DaySaleReportPojo.class);
        query.setFirstResult(page * pageSize);
        query.setMaxResults(pageSize);
        return query.getResultList();
    }

    public Long countDaySaleReportPojo() {
        TypedQuery<Long> query = getQuery(SELECT_COUNT, Long.class);
        return (Long) query.getSingleResult();
    }

    public List<DaySaleReportPojo> findByDateRange(ZonedDateTime startDate, ZonedDateTime endDate) {
        return getQuery(SELECT_DATE_FILTER, DaySaleReportPojo.class)
                .setParameter("start", startDate)
                .setParameter("end", endDate)
                .getResultList();
    }

    public DaySaleReportPojo findByDate(ZonedDateTime date) {
        List<DaySaleReportPojo> result = getQuery(SELECT_DATE, DaySaleReportPojo.class)
                .setParameter("date", date)
                .getResultList();
        return result.isEmpty() ? null : result.get(0);
    }

    public void update(DaySaleReportPojo sale) {
        em.merge(sale);
    }
}
