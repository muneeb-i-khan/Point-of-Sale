package com.increff.pos.service;

import com.increff.pos.db.dao.UserDao;
import com.increff.pos.db.pojo.UserPojo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {
    @Autowired
    private UserDao userDao;

    public Optional<UserPojo> get(Long id) {
        return userDao.findById(id);
    }

    public boolean getCheckEmail(String email) {
        return userDao.findByEmail(email).isPresent();
    }

    public void add(UserPojo userPojo) {
        userDao.save(userPojo);
    }

    public Optional<UserPojo> getUserByEmail(String email) {
        return userDao.findByEmail(email);
    }
}
