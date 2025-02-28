package com.increff.pos.db.dao;

import com.increff.pos.db.pojo.DaySaleReportPojo;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.time.LocalDate;
import java.util.List;

@Repository
public class DaySaleReportDao {
    @PersistenceContext
    private EntityManager entityManager;

    public void save(DaySaleReportPojo sale) {
        entityManager.persist(sale);
    }


    public List<DaySaleReportPojo> findByDateRange(LocalDate startDate, LocalDate endDate) {
        return entityManager.createQuery("SELECT p FROM ReportPojo p WHERE p.date BETWEEN :start AND :end",
                DaySaleReportPojo.class)
                .setParameter("start", startDate)
                .setParameter("end", endDate)
                .getResultList();
    }

    public DaySaleReportPojo findByDate(LocalDate date) {
        List<DaySaleReportPojo> result = entityManager.createQuery(
                        "SELECT p FROM ReportPojo p WHERE p.date = :date", DaySaleReportPojo.class)
                .setParameter("date", date)
                .getResultList();
        return result.isEmpty() ? null : result.get(0);
    }

    public void update(DaySaleReportPojo sale) {
        entityManager.merge(sale);
    }


}
