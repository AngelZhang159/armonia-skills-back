package com.armoniacode.armoniaskills.service;

import com.armoniacode.armoniaskills.dto.PerfilDTO;
import com.armoniacode.armoniaskills.entity.Review;
import com.armoniacode.armoniaskills.entity.Skill;
import com.armoniacode.armoniaskills.entity.Status;
import com.armoniacode.armoniaskills.entity.User;
import com.armoniacode.armoniaskills.repository.SkillRepository;
import com.armoniacode.armoniaskills.repository.UserRepository;
import com.armoniacode.armoniaskills.util.JWTUtil;
import org.springframework.http.HttpHeaders;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.UUID;


@Service
public class UserService {

    private final JWTUtil jwtUtil;
    private final UserRepository userRepository;
    private final SkillRepository skillRepository;

    public UserService(JWTUtil jwtUtil, UserRepository userRepository, SkillRepository skillRepository) {
        this.jwtUtil = jwtUtil;
        this.userRepository = userRepository;
        this.skillRepository = skillRepository;
    }

    public String registerUser(User user) {
        String password = user.getPassword();

        String encriptedPassword = BCrypt.hashpw(password, BCrypt.gensalt());

        user.setPassword(encriptedPassword);

        user.setRoles(Collections.singleton("USER"));

        User existingUser = userRepository.findByUsername(user.getUsername());
        if (existingUser != null) {
            throw new RuntimeException("Username already exists");
        }
        existingUser = userRepository.findByEmail(user.getEmail());
        if (existingUser != null) {
            throw new RuntimeException("Email already exists");
        }
        userRepository.save(user);
        return "User registered";
    }

    public HttpHeaders loginUser(User user) {
        User loggedInUser = userRepository.findByEmail(user.getEmail());

        if (loggedInUser == null) {
            String errorMessage = String.format("User with email: %s not found", user.getEmail());
            throw new RuntimeException(errorMessage);
        }

        if (!BCrypt.checkpw(user.getPassword(), loggedInUser.getPassword())) {
            throw new RuntimeException("Incorrect password");
        }

        String token = jwtUtil.getJWTToken(loggedInUser.getUsername());
        HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.set("Authorization", token);
        return responseHeaders;
    }

    public HttpHeaders loginUserJWT(String token) {

        try {
            String username = jwtUtil.getUsernameFromToken(token.substring(7));
            User user = userRepository.findByUsername(username);
            HttpHeaders responseHeaders = new HttpHeaders();
            String newToken = jwtUtil.getJWTToken(user.getUsername());
            responseHeaders.set("Authorization", newToken);
            return responseHeaders;
        } catch (RuntimeException e) {
            throw new RuntimeException("Incorrect JWT");
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

    public List<Review> getReviews(UUID id) {
        return userRepository.findById(id).orElse(null).getReviewList();
    }

    public PerfilDTO getPerfilById(UUID id) {
        User user = userRepository.findById(id).orElse(null);

        List<Skill> listaSkills = skillRepository.findAllByUserID(id);

        return new PerfilDTO(user.getUsername(), user.getImageURL(), user.getPhone(), user.getReviewList(), listaSkills);
    }
}
