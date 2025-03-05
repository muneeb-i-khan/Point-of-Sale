package com.increff.pos.db.dao;

import com.increff.pos.db.pojo.DaySaleReportPojo;
import com.increff.pos.db.pojo.InventoryPojo;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import java.time.ZonedDateTime;
import java.util.List;

@Repository
public class DaySaleReportDao {
    @PersistenceContext
    private EntityManager entityManager;

    public void save(DaySaleReportPojo sale) {
        entityManager.persist(sale);
    }

    public List<DaySaleReportPojo> selectAll() {
        return entityManager.createQuery("SELECT p FROM DaySaleReportPojo p", DaySaleReportPojo.class)
                .getResultList();
    }

    public List<DaySaleReportPojo> selectAllPaginated(int page, int pageSize) {
        TypedQuery<DaySaleReportPojo> query = entityManager.createQuery("SELECT p FROM DaySaleReportPojo p", DaySaleReportPojo.class);
        query.setFirstResult(page * pageSize);
        query.setMaxResults(pageSize);
        return query.getResultList();
    }

    public Long countDaySaleReportPojo() {
        Query query = entityManager.createQuery("SELECT COUNT(p) FROM DaySaleReportPojo p");
        return (Long) query.getSingleResult();
    }


    public List<DaySaleReportPojo> findByDateRange(ZonedDateTime startDate, ZonedDateTime endDate) {
        return entityManager.createQuery("SELECT p FROM DaySaleReportPojo p WHERE p.date BETWEEN :start AND :end",
                DaySaleReportPojo.class)
                .setParameter("start", startDate)
                .setParameter("end", endDate)
                .getResultList();
    }

    public DaySaleReportPojo findByDate(ZonedDateTime date) {
        List<DaySaleReportPojo> result = entityManager.createQuery(
                        "SELECT p FROM DaySaleReportPojo p WHERE p.date = :date", DaySaleReportPojo.class)
                .setParameter("date", date)
                .getResultList();
        return result.isEmpty() ? null : result.get(0);
    }

    public void update(DaySaleReportPojo sale) {
        entityManager.merge(sale);
    }


}
