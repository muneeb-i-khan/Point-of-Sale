package com.increff.pos.service;

import com.increff.pos.db.dao.UserDao;
import com.increff.pos.db.pojo.UserPojo;
import com.increff.pos.dto.UserDto;
import com.increff.pos.model.constants.Role;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;
import java.util.Optional;

import static org.junit.Assert.*;

@ContextConfiguration(classes = QaConfig.class)
@Transactional
@Rollback
public class AuthServiceTest extends AbstractUnitTest{

    @Autowired
    private AuthService authService;

    @Autowired
    private UserDao userDao;

    private MockHttpSession session;

    @Before
    public void setUp() {
        session = new MockHttpSession();
    }

    @Test
    public void testLoginSuccess() {
        // Given
        UserPojo user = new UserPojo();
        user.setEmail("test@example.com");
        user.setPassword(authService.passwordEncoder.encode("password123"));
        user.setRole(Role.SUPERVISOR);
        userDao.save(user);

        UserDto result = authService.login("test@example.com", "password123", session);

        assertNotNull(result);
        assertEquals("test@example.com", result.getEmail());
        assertEquals(Role.SUPERVISOR, result.getRole());
        assertEquals(user.getId(), session.getAttribute("userId"));
    }

    @Test
    public void testLogin_FailureInvalidPassword() {
        UserPojo user = new UserPojo();
        user.setEmail("test@example.com");
        user.setPassword(authService.passwordEncoder.encode("password123"));
        user.setRole(Role.SUPERVISOR);
        userDao.save(user);
        UserDto result = authService.login("test@example.com", "wrongpassword", session);

        assertNull(result);
        assertNull(session.getAttribute("userId"));
    }

    @Test
    public void testLogin_FailureUserNotFound() {
        UserDto result = authService.login("nonexistent@example.com", "password123", session);
        assertNull(result);
    }

    @Test
    public void testGetSessionUserSuccess() {
        UserPojo user = new UserPojo();
        user.setEmail("test@example.com");
        user.setPassword(authService.passwordEncoder.encode("password123"));
        user.setRole(Role.OPERATOR);
        userDao.save(user);
        session.setAttribute("userId", user.getId());

        UserDto result = authService.getSessionUser(session);

        assertNotNull(result);
        assertEquals("test@example.com", result.getEmail());
        assertEquals(Role.OPERATOR, result.getRole());
    }

    @Test
    public void testGetSessionUser_NoSession() {
        UserDto result = authService.getSessionUser(session);
        assertNull(result);
    }

    @Test
    public void testLogout() {
        session.setAttribute("userId", 1L);
        session.setAttribute("role", Role.SUPERVISOR);
        authService.logout(session);
        try {
            session.getAttribute("userId");
            fail("Expected IllegalStateException due to invalidated session");
        } catch (IllegalStateException e) {
        }
    }


    @Test
    public void testRegisterUserSuccess() {
        ResponseEntity<Map<String, String>> response = authService.registerUser("newuser@example.com", "password123", "SUPERVISOR");

        assertEquals(200, response.getStatusCodeValue());
        assertEquals("User registered successfully", response.getBody().get("message"));

        Optional<UserPojo> savedUser = userDao.findByEmail("newuser@example.com");
        assertTrue(savedUser.isPresent());
        assertTrue(authService.passwordEncoder.matches("password123", savedUser.get().getPassword()));
    }

    @Test
    public void testRegisterUserEmailAlreadyExists() {
        UserPojo user = new UserPojo();
        user.setEmail("existing@example.com");
        user.setPassword(authService.passwordEncoder.encode("password123"));
        user.setRole(Role.SUPERVISOR);
        userDao.save(user);

        ResponseEntity<Map<String, String>> response = authService.registerUser("existing@example.com", "password123", "SUPERVISOR");

        assertEquals(409, response.getStatusCodeValue());
        assertEquals("Email already exists", response.getBody().get("message"));
    }

    @Test
    public void testRegisterUserInvalidRole() {
        ResponseEntity<Map<String, String>> response = authService.registerUser("invalidrole@example.com", "password123", "INVALID_ROLE");
        assertEquals(400, response.getStatusCodeValue());
        assertEquals("Invalid role", response.getBody().get("message"));
    }
}
