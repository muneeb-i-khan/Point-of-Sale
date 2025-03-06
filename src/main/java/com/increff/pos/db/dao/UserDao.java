package com.increff.pos.db.dao;

import com.increff.pos.db.pojo.UserPojo;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.transaction.Transactional;
import java.util.Optional;

@Repository
public class UserDao {

    @PersistenceContext
    private EntityManager em;

    private static String SELECT_EMAIL = "SELECT u FROM UserPojo u WHERE u.email = :email";

    public Optional<UserPojo> findByEmail(String email) {
        TypedQuery<UserPojo> query = getQuery(SELECT_EMAIL);
        query.setParameter("email", email);
        return query.getResultList().stream().findFirst();
    }

    @Transactional
    public void save(UserPojo user) {
        em.persist(user);
    }

    public Optional<UserPojo> findById(Long id) {
        return Optional.ofNullable(em.find(UserPojo.class, id));
    }

    TypedQuery<UserPojo> getQuery(String jpql) {
        return em.createQuery(jpql, UserPojo.class);
    }
}
