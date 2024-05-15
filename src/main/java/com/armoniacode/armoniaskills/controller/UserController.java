package com.armoniacode.armoniaskills.controller;

import com.armoniacode.armoniaskills.entity.User;
import com.armoniacode.armoniaskills.service.UserService;
import com.armoniacode.armoniaskills.util.JWTUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/v1")
public class UserController {

    private final JWTUtil jwtUtil;

    private static final Logger logger = LoggerFactory.getLogger(LoginController.class);
    private final UserService userService;

    public UserController(JWTUtil jwtUtil, UserService userService) {
        this.jwtUtil = jwtUtil;
        this.userService = userService;
    }

    @GetMapping("/user/data")
    public ResponseEntity<User> userData(@RequestHeader String Authorization) {

        String token = Authorization.substring(7);

        Optional<User> user = jwtUtil.getUserFromToken(token);

        logger.info("User data: {}", user.toString());

        if (user.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        } else {
            return new ResponseEntity<>(user.get(), HttpStatus.OK);
        }

    }

    @PatchMapping("/user/updateUser")
    public ResponseEntity<String> updateUser(@RequestHeader String Authorization, @RequestBody User user) {

        String token = Authorization.substring(7);

        Optional<User> userFromToken = jwtUtil.getUserFromToken(token);

        if (userFromToken.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }

        User userToUpdate = userFromToken.get();

        if (user.getPassword() != null) {
            String password = user.getPassword();
            String encryptedPassword = BCrypt.hashpw(password, BCrypt.gensalt());
            userToUpdate.setPassword(encryptedPassword);
        }

        if (user.getFullName() != null) {
            userToUpdate.setFullName(user.getFullName());
        }

        if (user.getUsername() != null) {
            userToUpdate.setUsername(user.getUsername());
        }

        if (user.getPhone() != 0){
            userToUpdate.setPhone(user.getPhone());
        }

        userService.save(userToUpdate);

        return new ResponseEntity<>("User updated successfully", HttpStatus.OK);
    }
}
