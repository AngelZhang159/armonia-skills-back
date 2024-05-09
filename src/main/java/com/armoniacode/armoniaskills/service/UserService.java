package com.armoniacode.armoniaskills.service;

import com.armoniacode.armoniaskills.entity.User;
import com.armoniacode.armoniaskills.repository.UserRepository;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Service;


@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User registerUser(User user) {
        return userRepository.save(user);
    }

    public String loginUser(User user) {
        User userFromDB = userRepository.findByEmail(user.getEmail());
        if (userFromDB != null) {
            if (BCrypt.checkpw(user.getContrasenia(), userFromDB.getContrasenia())) {
                return "Login exitoso";
            } else {
                return "Contraseña incorrecta";
            }
        } else {
            return "Usuario no encontrado";
        }
    }
}
