package com.increff.pos.service;

import com.increff.pos.db.dao.UserDao;
import com.increff.pos.db.pojo.UserPojo;
import com.increff.pos.model.data.UserData;
import com.increff.pos.util.ApiException;
import com.increff.pos.util.RoleAssigner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.naming.AuthenticationException;
import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
@Service
public class AuthService {
    @Autowired
    private UserDao userDao;

    @Autowired
    private AuthenticationManager authenticationManager;

    private final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    public UserData login(String email, String password, HttpSession session) throws ApiException {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(email, password)
        );
        SecurityContextHolder.getContext().setAuthentication(authentication);

        Optional<UserPojo> userOpt = userDao.findByEmail(email);
        UserPojo user = userOpt.orElseThrow(() -> new ApiException("User not found"));

        session.setAttribute("userId", user.getId());
        session.setAttribute("role", user.getRole());
        return new UserData(user.getId(), user.getEmail(), user.getRole());
    }

    public UserData getSessionUser(HttpSession session) throws ApiException {
        Long userId = (Long) session.getAttribute("userId");
        if (userId == null) {
            throw new ApiException("No active session");
        }
        Optional<UserPojo> userOpt = userDao.findById(userId);
        return userOpt.map(user -> new UserData(user.getId(), user.getEmail(), user.getRole()))
                .orElseThrow(() -> new ApiException("User not found"));
    }

    public void registerUser(String email, String password) throws ApiException {
        if (userDao.findByEmail(email).isPresent()) {
            throw new ApiException("Email already exists");
        }
        try {
            UserPojo newUser = new UserPojo();
            newUser.setEmail(email);
            newUser.setPassword(passwordEncoder.encode(password));
            newUser.setRole(RoleAssigner.assignRole(email));
            userDao.save(newUser);
        } catch (Exception e) {
            throw new ApiException("Error registering user: " + e.getMessage());
        }
    }

    public Optional<UserPojo> getUserByEmail(String email) {
        return userDao.findByEmail(email);
    }
}