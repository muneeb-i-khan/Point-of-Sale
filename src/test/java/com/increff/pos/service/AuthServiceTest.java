package com.increff.pos.service;

import com.increff.pos.db.dao.UserDao;
import com.increff.pos.db.pojo.UserPojo;
import com.increff.pos.model.data.UserData;
import com.increff.pos.util.ApiException;
import com.increff.pos.util.RoleAssigner;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static org.junit.Assert.*;

@ContextConfiguration(classes = QaConfig.class)
@Transactional
@Rollback
public class AuthServiceTest extends AbstractUnitTest {

    @Autowired
    private AuthService authService;

    @Autowired
    private UserDao userDao;

    @Autowired
    private AuthenticationManager authenticationManager;

    private MockHttpSession session;
    private final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    @Before
    public void setUp() {
        session = new MockHttpSession();
    }

    @Test
    public void testLoginSuccessSupervisor() throws ApiException {
        UserPojo user = new UserPojo();
        user.setEmail("supervisor@increff.com");
        user.setPassword(passwordEncoder.encode("password123"));
        user.setRole(RoleAssigner.assignRole("supervisor@increff.com"));
        userDao.save(user);

        UserData result = authService.login("supervisor@increff.com", "password123", session);

        assertNotNull(result);
        assertEquals("supervisor@increff.com", result.getEmail());
        assertEquals(user.getRole(), result.getRole());
        assertEquals(user.getId(), session.getAttribute("userId"));
    }

    @Test
    public void testLoginSuccessOperator() throws ApiException {
        UserPojo user = new UserPojo();
        user.setEmail("operator@example.com");
        user.setPassword(passwordEncoder.encode("password123"));
        user.setRole(RoleAssigner.assignRole("operator@example.com"));
        userDao.save(user);

        UserData result = authService.login("operator@example.com", "password123", session);

        assertNotNull(result);
        assertEquals("operator@example.com", result.getEmail());
        assertEquals(user.getRole(), result.getRole());
        assertEquals(user.getId(), session.getAttribute("userId"));
    }

    @Test
    public void testLogin_FailureInvalidPassword() {
        UserPojo user = new UserPojo();
        user.setEmail("test@example.com");
        user.setPassword(passwordEncoder.encode("password123"));
        user.setRole(RoleAssigner.assignRole("test@example.com"));
        userDao.save(user);

        try {
            authService.login("test@example.com", "wrongpassword", session);
            fail("Expected ApiException for incorrect password");
        } catch (ApiException e) {
            assertEquals("Bad credentials", e.getMessage());
        }
        assertNull(session.getAttribute("userId"));
    }

    @Test
    public void testLogin_FailureUserNotFound() {
        try {
            authService.login("nonexistent@example.com", "password123", session);
            fail("Expected ApiException for non-existent user");
        } catch (ApiException e) {
            assertEquals("Bad credentials", e.getMessage());
        }
    }

    @Test
    public void testGetSessionUserSuccess() throws ApiException {
        UserPojo user = new UserPojo();
        user.setEmail("test@example.com");
        user.setPassword(passwordEncoder.encode("password123"));
        user.setRole(RoleAssigner.assignRole("test@example.com"));
        userDao.save(user);
        session.setAttribute("userId", user.getId());

        UserData result = authService.getSessionUser(session);

        assertNotNull(result);
        assertEquals("test@example.com", result.getEmail());
        assertEquals(user.getRole(), result.getRole());
    }

    @Test
    public void testGetSessionUser_NoSession() {
        try {
            authService.getSessionUser(session);
            fail("Expected ApiException for no active session");
        } catch (ApiException e) {
            assertEquals("No active session", e.getMessage());
        }
    }

    @Test
    public void testRegisterUserSuccess() throws ApiException {
        authService.registerUser("newuser@example.com", "password123");

        Optional<UserPojo> savedUser = userDao.findByEmail("newuser@example.com");
        assertTrue(savedUser.isPresent());
        assertTrue(passwordEncoder.matches("password123", savedUser.get().getPassword()));
    }

    @Test
    public void testRegisterUserEmailAlreadyExists() {
        UserPojo user = new UserPojo();
        user.setEmail("existing@example.com");
        user.setPassword(passwordEncoder.encode("password123"));
        user.setRole(RoleAssigner.assignRole("existing@example.com"));
        userDao.save(user);

        try {
            authService.registerUser("existing@example.com", "password123");
            fail("Expected ApiException for duplicate email");
        } catch (ApiException e) {
            assertEquals("Email already exists", e.getMessage());
        }
    }
}
