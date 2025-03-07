package com.increff.pos.controller;

import com.increff.pos.model.data.UserData;
import com.increff.pos.model.forms.UserForm;
import com.increff.pos.service.AuthService;
import com.increff.pos.util.ApiException;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private AuthService authService;

    @ApiOperation(value = "Login")
    @PostMapping("/login")
    public UserData login(@RequestBody UserForm loginRequest, HttpSession session) throws ApiException {
        return authService.login(loginRequest.getEmail(), loginRequest.getPassword(), session);
    }

    @ApiOperation(value = "Check session")
    @GetMapping("/check")
    public UserData checkSession(HttpSession session) throws ApiException {
        return authService.getSessionUser(session);
    }

    @ApiOperation(value = "Logout")
    @PostMapping("/logout")
    public void logout(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session != null) {
            session.invalidate();
        }
    }

    @ApiOperation(value = "Sign up")
    @PostMapping("/signup")
    public void signup(@Valid @RequestBody UserForm signupRequest) throws ApiException {
        authService.registerUser(signupRequest.getEmail(), signupRequest.getPassword());
    }
}
