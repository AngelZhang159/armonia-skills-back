package com.armoniacode.armoniaskills.controller;

import com.armoniacode.armoniaskills.entity.Users;
import com.armoniacode.armoniaskills.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/api/v1")
@RestController
@Slf4j
public class LoginController {

    private final UserService userService;

    public LoginController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/user/login")
    public ResponseEntity<String> loginUser(@RequestBody Users user) {
        try {
            HttpHeaders responseHeaders = userService.loginUser(user);
            return new ResponseEntity<>("User logged in successfully", responseHeaders, HttpStatus.OK);

        } catch (RuntimeException e) {
            log.error("Error during login: {}", e.getMessage());
            return new ResponseEntity<>(e.getMessage(), HttpStatus.UNAUTHORIZED);
        }
    }

    @PostMapping("/user/loginJWT")
    public ResponseEntity<String> loginUserJWT(@RequestHeader String Authorization) {
        try {
            HttpHeaders responseHeaders = userService.loginUserJWT(Authorization);
            return new ResponseEntity<>("User logged in successfully", responseHeaders, HttpStatus.OK);

        } catch (RuntimeException e) {
            log.error("Error during login: {}", e.getMessage());
            return new ResponseEntity<>(e.getMessage(), HttpStatus.UNAUTHORIZED);
        }
    }
}
