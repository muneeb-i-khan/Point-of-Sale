package com.increff.pos.controller;

import com.increff.pos.model.data.UserData;
import com.increff.pos.model.forms.UserForm;
import com.increff.pos.service.AuthService;
import com.increff.pos.util.ApiException;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
    public ResponseEntity<UserData> login(@RequestBody UserForm loginRequest, HttpSession session) {
        try {
            UserData userData = authService.login(loginRequest.getEmail(), loginRequest.getPassword(), session);
            return ResponseEntity.ok(userData);
        } catch (ApiException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }
    }

    @ApiOperation(value = "Check session")
    @GetMapping("/check")
    public ResponseEntity<UserData> checkSession(HttpSession session) {
        try {
            UserData userData = authService.getSessionUser(session);
            return ResponseEntity.ok(userData);
        } catch (ApiException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }
    }

    @ApiOperation(value = "Logout")
    @PostMapping("/logout")
    public ResponseEntity<Void> logout(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session != null) {
            session.invalidate();
        }
        return ResponseEntity.ok().build();
    }

    @ApiOperation(value = "Sign up")
    @PostMapping("/signup")
    public ResponseEntity<Void> signup(@Valid @RequestBody UserForm signupRequest) {
        try {
            authService.registerUser(signupRequest.getEmail(), signupRequest.getPassword());
            return ResponseEntity.ok().build();
        } catch (ApiException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }
}