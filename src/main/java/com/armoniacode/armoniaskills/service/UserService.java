package com.armoniacode.armoniaskills.service;

import com.armoniacode.armoniaskills.entity.Status;
import com.armoniacode.armoniaskills.entity.User;
import com.armoniacode.armoniaskills.repository.UserRepository;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;


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
        userToUpdate.setStatus(Status.ONLINE);
        userRepository.save(userToUpdate);
    }

    public void disconnect(User userToUpdate) {
        User user = userRepository.findById(userToUpdate.getId()).orElse(null);

        if (user != null) {
            user.setStatus(Status.OFFLINE);
            userRepository.save(user);
        }
    }

    public List<User> getAllUsers() {
        return userRepository.findByStatus(Status.ONLINE);
    }

    public User getUserById(UUID id) {
        return userRepository.findById(id).orElse(null);
    }
}
