package com.armoniacode.armoniaskills.controller;

import com.armoniacode.armoniaskills.entity.Review;
import com.armoniacode.armoniaskills.entity.User;
import com.armoniacode.armoniaskills.service.UserService;
import com.armoniacode.armoniaskills.util.JWTUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

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

        if (user.getImageURL() != null) {
            userToUpdate.setImageURL(user.getImageURL());
        }

        userService.save(userToUpdate);

        return new ResponseEntity<>("User updated successfully", HttpStatus.OK);
    }

    @MessageMapping("/user.addUser")
    @SendTo("/user/topic")
    public User addUser(@Payload User user) {
        userService.save(user);
        return user;
    }

    @MessageMapping("/user.disconnectUser")
    @SendTo("/user/topic")
    public User disconnectUser(@Payload User user) {
        userService.disconnect(user);
        return user;
    }

    @GetMapping("/user/allUsers")
    public ResponseEntity<List> getAllUsers(@RequestHeader String Authorization) {

        String token = Authorization.substring(7);

        Optional<User> user = jwtUtil.getUserFromToken(token);

        if (user.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }

        return new ResponseEntity<>(userService.getAllUsers(), HttpStatus.OK);
    }

    @PatchMapping("user/addReview")
    public ResponseEntity<String> addReview(@RequestHeader String Authorization, @RequestBody Review review){

        String token = Authorization.substring(7);

        Optional<User> userFromToken = jwtUtil.getUserFromToken(token);

        if (userFromToken.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }

        User userToUpdate = getUserById(review.getSellerId());

        logger.info(review.toString());

        User userBuyer = userFromToken.get();
        review.setBuyerId(userBuyer.getId());
        review.setImageUrl(userBuyer.getImageURL());
        review.setUsername(userBuyer.getUsername());

        userToUpdate.addReview(review);

        userService.save(userToUpdate);

        return new ResponseEntity<>("Review: " + review.getContent() + "  added succesfully to user: " + userToUpdate.getUsername(), HttpStatus.OK);

    }

    @GetMapping("/user")
    public User getUserById(@RequestParam UUID id) {
        //DEVUELVE USUARIO ENTERO, QUITAR LUEGO COSAS QUE NO DEBERIAN ESTAR
        return userService.getUserById(id);
    }

    @GetMapping("/user/id")
    public ResponseEntity<UUID> getUserId(@RequestHeader String Authorization) {

        String token = Authorization.substring(7);

        Optional<User> user = jwtUtil.getUserFromToken(token);

        if (user.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }

        return new ResponseEntity<>(user.get().getId(), HttpStatus.OK);
    }
}
