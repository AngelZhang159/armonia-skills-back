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

    public String registerUser(User user) {
        User existingUser = userRepository.findByUsername(user.getUsername());
        if (existingUser != null) {
            return "Username already exists";
        }
        existingUser = userRepository.findByEmail(user.getEmail());
        if (existingUser != null) {
            return "Email already exists";
        }
        userRepository.save(user);
        return "User registered";
    }

    public User loginUser(User user) {
        User userFromDB = userRepository.findByEmail(user.getEmail());
        if (userFromDB != null) {
            if (BCrypt.checkpw(user.getPassword(), userFromDB.getPassword())) {
                return userFromDB;
            } else {
                throw new RuntimeException("Incorrect password");
            }
        } else {
            throw new RuntimeException("User not found");
        }
    }

    public void save(User userToUpdate) {
        userRepository.save(userToUpdate);
    }
}
