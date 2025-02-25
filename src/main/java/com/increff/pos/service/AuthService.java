package com.increff.pos.service;

import com.increff.pos.db.dao.UserDao;
import com.increff.pos.db.pojo.UserPojo;
import com.increff.pos.dto.UserDto;
import com.increff.pos.model.constants.Role;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Service
public class AuthService {
    private final UserDao userDao;
    private final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    public AuthService(UserDao userDao) {
        this.userDao = userDao;
    }

    public UserDto login(String email, String password, HttpSession session) {
        Optional<UserPojo> userOpt = userDao.findByEmail(email);
        if (userOpt.isPresent()) {
            UserPojo user = userOpt.get();
            if (passwordEncoder.matches(password, user.getPassword())) {
                session.setAttribute("userId", user.getId());
                session.setAttribute("role", user.getRole());
                return new UserDto(user.getId(), user.getEmail(), user.getRole());
            }
        }
        return null;
    }

    public UserDto getSessionUser(HttpSession session) {
        Long userId = (Long) session.getAttribute("userId");
        if (userId != null) {
            Optional<UserPojo> userOpt = userDao.findById(userId);
            if (userOpt.isPresent()) {
                UserPojo user = userOpt.get();
                return new UserDto(user.getId(), user.getEmail(), user.getRole());
            }
        }
        return null;
    }

    public void logout(HttpSession session) {
        session.invalidate();
    }

    public ResponseEntity<Map<String, String>> registerUser(String email, String password, String role) {
        Map<String, String> response = new HashMap<>();

        if (userDao.findByEmail(email).isPresent()) {
            response.put("message", "Email already exists");
            return ResponseEntity.status(HttpStatus.CONFLICT).body(response);
        }

        try {
            Role userRole = Role.valueOf(role.toUpperCase());
            UserPojo newUser = new UserPojo();
            newUser.setEmail(email);
            newUser.setPassword(passwordEncoder.encode(password));
            newUser.setRole(userRole);
            userDao.save(newUser);

            response.put("message", "User registered successfully");
            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException e) {
            response.put("message", "Invalid role");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        } catch (Exception e) {
            response.put("message", "An error occurred while registering the user");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

}
