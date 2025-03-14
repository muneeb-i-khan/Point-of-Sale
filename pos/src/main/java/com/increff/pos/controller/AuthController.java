package com.increff.pos.controller;

import com.increff.pos.model.data.UserData;
import com.increff.pos.model.forms.UserForm;
import com.increff.pos.flow.AuthFlow;
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
    private AuthFlow authFlow;

    @PostMapping(value = "/login", consumes = "application/x-www-form-urlencoded")
    public UserData login(@ModelAttribute @Valid UserForm loginRequest, HttpSession session) throws ApiException {
        return authFlow.login(loginRequest.getEmail(), loginRequest.getPassword(), session);
    }

    @PostMapping(value = "/signup", consumes = "application/x-www-form-urlencoded")
    public void signup(@ModelAttribute @Valid UserForm signupRequest) throws ApiException {
        authFlow.registerUser(signupRequest.getEmail(), signupRequest.getPassword());
    }

    @ApiOperation(value = "Check session")
    @GetMapping("/check")
    public UserData checkSession(HttpSession session) throws ApiException {
        return authFlow.getSessionUser(session);
    }

    @ApiOperation(value = "Logout")
    @PostMapping("/logout")
    public void logout(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session != null) {
            session.invalidate();
        }
    }
}
