package com.increff.pos.controller;

import com.increff.pos.model.data.UserData;
import com.increff.pos.model.forms.UserForm;
import com.increff.pos.service.AuthService;
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
    public ResponseEntity<?> login(@RequestBody UserForm loginRequest, HttpSession session) {
        UserData user = authService.login(loginRequest.getEmail(), loginRequest.getPassword(), session);
        if (user != null) {
            return ResponseEntity.ok(user);
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid email or password");
    }

    @ApiOperation(value = "check session")
    @GetMapping("/check")
    public ResponseEntity<?> checkSession(HttpSession session) {
        UserData user = authService.getSessionUser(session);
        if (user != null) {
            return ResponseEntity.ok(user);
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Not authenticated");
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
    public void signup(@Valid @RequestBody UserForm signupRequest) {
        authService.registerUser(signupRequest.getEmail(), signupRequest.getPassword());
    }
}
