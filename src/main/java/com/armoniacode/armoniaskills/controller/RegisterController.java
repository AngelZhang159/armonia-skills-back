package com.armoniacode.armoniaskills.controller;

import com.armoniacode.armoniaskills.entity.User;
import com.armoniacode.armoniaskills.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/api/v1")
@RestController
public class RegisterController {

    private final UserService userService;

    public RegisterController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/user/register")
    public ResponseEntity<User> registerUser(@RequestBody User user) {

        String contrasenia = user.getContrasenia();

        String contraseniaEncriptada = BCrypt.hashpw(contrasenia, BCrypt.gensalt());

        user.setContrasenia(contraseniaEncriptada);

        return new ResponseEntity<>(userService.registerUser(user), HttpStatus.CREATED);

    }
}
