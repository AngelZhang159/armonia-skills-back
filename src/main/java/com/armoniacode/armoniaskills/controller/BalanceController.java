package com.armoniacode.armoniaskills.controller;

import com.armoniacode.armoniaskills.util.JWTUtil;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@Controller
@RequestMapping("/api/v1")
public class BalanceController {

    private final JWTUtil jwtUtil;

    public BalanceController(JWTUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

    @GetMapping("/balance")
    public ResponseEntity<Double> getBalance(@RequestHeader("Authorization") String Authorization) {

        String token = Authorization.substring(7);

        return new ResponseEntity<>(jwtUtil.getBalance(token), org.springframework.http.HttpStatus.OK);
    }

    @PostMapping("/balance")
    public ResponseEntity<String> updateBalance(@RequestHeader("Authorization") String Authorization, @RequestBody Double balance) {

        String token = Authorization.substring(7);
        jwtUtil.updateBalance(token, balance);

        return new ResponseEntity<>("User balance updated", org.springframework.http.HttpStatus.OK);
    }

    @PostMapping("/balance/transfer")
    public ResponseEntity<Double> transferBalance(@RequestHeader("Authorization") String Authorization, @RequestBody UUID toUserID, @RequestBody Double balance) {

        String token = Authorization.substring(7);

        return new ResponseEntity<>(jwtUtil.transferBalance(token, toUserID, balance), org.springframework.http.HttpStatus.OK);
    }
}
