package com.armoniacode.armoniaskills.util;

import com.armoniacode.armoniaskills.controller.LoginController;
import com.armoniacode.armoniaskills.entity.User;
import com.armoniacode.armoniaskills.repository.UserRepository;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.security.Key;
import java.util.*;
import java.util.stream.Collectors;

@Configuration
public class JWTUtil {

    private static final String SECRET_KEY = "1e3e9a52af68343a63521fd8b6825584fc9dbe634ac81060f5cfa0b3b526daee"; //Generado con SHA256: ArmoniaSkills
    private static final long TOKEN_EXPIRATION_TIME = 7; // 7 days
    private final UserRepository userRepository;

    private static final Logger logger = LoggerFactory.getLogger(LoginController.class);

    public JWTUtil(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public String getJWTToken(String username) {
        User user = userRepository.findByUsername(username);

        if (user == null) {
            logger.error("User not found");
            throw new UsernameNotFoundException("User not found");
        }

        List<GrantedAuthority> grantedAuthorities = AuthorityUtils
                .commaSeparatedStringToAuthorityList("ROLE_USER");

        byte[] decodedKey = Base64.getDecoder().decode(SECRET_KEY);
        Key key = new SecretKeySpec(decodedKey, 0, decodedKey.length, "HmacSHA256");

        String token = Jwts
                .builder()
                .id(user.getId().toString())
                .subject(username)
                .claim("authorities",
                        grantedAuthorities.stream()
                                .map(GrantedAuthority::getAuthority)
                                .collect(Collectors.toList()))
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + 1000L * 60 * 60 * 24 * TOKEN_EXPIRATION_TIME))
                .signWith(key).compact();

        return "Bearer " + token;
    }

    public boolean validateToken(String token) {
        try {
            byte[] decodedKey = Base64.getDecoder().decode(SECRET_KEY);
            SecretKey key = new SecretKeySpec(decodedKey, 0, decodedKey.length, "HmacSHA256");
            Jwts.parser().verifyWith(key).build().parseSignedClaims(token);
            return true;
        } catch (JwtException e) {
            return false;
        }
    }

    public String getUsernameFromToken(String token) {
        byte[] decodedKey = Base64.getDecoder().decode(SECRET_KEY);
        SecretKey key = new SecretKeySpec(decodedKey, 0, decodedKey.length, "HmacSHA256");
        return Jwts.parser().verifyWith(key).build().parseSignedClaims(token).getPayload().getSubject();
    }


    public Optional<User> getUserFromToken(String token) {
        byte[] decodedKey = Base64.getDecoder().decode(SECRET_KEY);
        SecretKey key = new SecretKeySpec(decodedKey, 0, decodedKey.length, "HmacSHA256");

        UUID uuid = UUID.fromString(Jwts.parser().verifyWith(key).build().parseSignedClaims(token).getPayload().getId());

        logger.info("UUID: {}", uuid);

        return userRepository.findById(uuid);
    }

    public UUID getUUID(String token) {

        byte[] decodedKey = Base64.getDecoder().decode(SECRET_KEY);
        SecretKey key = new SecretKeySpec(decodedKey, 0, decodedKey.length, "HmacSHA256");

        UUID uuid = UUID.fromString(Jwts.parser().verifyWith(key).build().parseSignedClaims(token).getPayload().getId());

        return uuid;

    }

    public double getBalance(String token) {
        byte[] decodedKey = Base64.getDecoder().decode(SECRET_KEY);
        SecretKey key = new SecretKeySpec(decodedKey, 0, decodedKey.length, "HmacSHA256");

        UUID uuid = UUID.fromString(Jwts.parser().verifyWith(key).build().parseSignedClaims(token).getPayload().getId());
        User user = userRepository.findById(uuid).orElse(null);

        if (user != null) {
            return user.getBalance();
        } else {
            return 0;
        }
    }

    public void updateBalance(String token, Double balance) {
        byte[] decodedKey = Base64.getDecoder().decode(SECRET_KEY);
        SecretKey key = new SecretKeySpec(decodedKey, 0, decodedKey.length, "HmacSHA256");

        UUID uuid = UUID.fromString(Jwts.parser().verifyWith(key).build().parseSignedClaims(token).getPayload().getId());
        User user = userRepository.findById(uuid).orElse(null);

        if (user != null) {
            userRepository.save(user).setBalance(balance);
        }
    }

    public double transferBalance(String token, UUID toUserID, Double balance) {
        byte[] decodedKey = Base64.getDecoder().decode(SECRET_KEY);
        SecretKey key = new SecretKeySpec(decodedKey, 0, decodedKey.length, "HmacSHA256");

        UUID fromUserID = UUID.fromString(Jwts.parser().verifyWith(key).build().parseSignedClaims(token).getPayload().getId());

        return 0;
    }

    public void depositBalance(String token, Double balance) {

        byte[] decodedKey = Base64.getDecoder().decode(SECRET_KEY);
        SecretKey key = new SecretKeySpec(decodedKey, 0, decodedKey.length, "HmacSHA256");

        UUID uuid = UUID.fromString(Jwts.parser().verifyWith(key).build().parseSignedClaims(token).getPayload().getId());
        User user = userRepository.findById(uuid).orElse(null);

        if (user != null) {
            user.setBalance(user.getBalance() + balance);
            userRepository.save(user);
        }
    }

    public void withdrawBalance(String token, Double balance) {

        byte[] decodedKey = Base64.getDecoder().decode(SECRET_KEY);
        SecretKey key = new SecretKeySpec(decodedKey, 0, decodedKey.length, "HmacSHA256");

        UUID uuid = UUID.fromString(Jwts.parser().verifyWith(key).build().parseSignedClaims(token).getPayload().getId());
        User user = userRepository.findById(uuid).orElse(null);

        double balanceActual = user.getBalance();

        if (balanceActual < balance) {
            throw new RuntimeException("Insufficient balance");
        } else {
            if (user != null) {
                user.setBalance((user.getBalance() - balance));
                userRepository.save(user);
            }
        }
    }
}