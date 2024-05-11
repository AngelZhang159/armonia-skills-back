package com.armoniacode.armoniaskills.controller;

import com.armoniacode.armoniaskills.entity.User;
import com.armoniacode.armoniaskills.service.UserService;
import com.armoniacode.armoniaskills.util.JWTUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/api/v1")
@RestController
public class LoginController {

    private final UserService userService;
    private final JWTUtil jwtUtil;

    private static final Logger logger = LoggerFactory.getLogger(LoginController.class);

    public LoginController(UserService userService, JWTUtil jwtUtil) {
        this.userService = userService;
        this.jwtUtil = jwtUtil;
    }

    @PostMapping("/user/login")
    public ResponseEntity<String> loginUser(@RequestBody User user) {
        try {
            User loggedInUser = userService.loginUser(user);
            String token = jwtUtil.getJWTToken(loggedInUser.getUsername());
            HttpHeaders responseHeaders = new HttpHeaders();
            responseHeaders.set("Authorization", token);

            return new ResponseEntity<>("User logged in successfully", responseHeaders, HttpStatus.OK);

        } catch (RuntimeException e) {
            logger.error("Error during login", e);
            return new ResponseEntity<>(null, HttpStatus.UNAUTHORIZED);
        }
    }
}
