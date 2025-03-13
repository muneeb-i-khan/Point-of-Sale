package com.increff.pos.service;

import com.increff.pos.db.dao.UserDao;
import com.increff.pos.db.pojo.UserPojo;
import com.increff.pos.model.data.UserData;
import com.increff.pos.util.ApiException;
import com.increff.pos.util.RoleAssigner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import com.increff.pos.util.Constants;

import javax.servlet.http.HttpSession;
import java.util.Optional;
@Service
//TODO: make it a flow layer
public class AuthService {
    @Autowired
    private UserDao userDao;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private Constants constants;

    private final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    public UserData login(String email, String password, HttpSession session) throws ApiException {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(email, password)
            );
            SecurityContextHolder.getContext().setAuthentication(authentication);
        } catch (org.springframework.security.authentication.BadCredentialsException e) {
            throw new ApiException("Bad credentials");
        }

        Optional<UserPojo> userOpt = userDao.findByEmail(email);
        UserPojo user = userOpt.orElseThrow(() -> new ApiException("User not found"));

        //TODO; to create a constants class and move there
        session.setAttribute(constants.USER_ID, user.getId());
        session.setAttribute(constants.ROLE, user.getRole());
        return new UserData(user.getId(), user.getEmail(), user.getRole());
    }

    public UserData getSessionUser(HttpSession session) throws ApiException {
        Long userId = (Long) session.getAttribute(constants.USER_ID);
        if (userId == null) {
            throw new ApiException("No active session");
        }
        Optional<UserPojo> userOpt = userDao.findById(userId);
        return userOpt.map(user -> new UserData(user.getId(), user.getEmail(), user.getRole()))
                .orElseThrow(() -> new ApiException("User not found"));
    }

    public void registerUser(String email, String password) throws ApiException {
        //TODO: dont call userdao directly instead call userApi
        if (userDao.findByEmail(email).isPresent()) {
            throw new ApiException("Email already exists");
        }
        //TODO: to remove the try catch
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