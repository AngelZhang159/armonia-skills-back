package com.armoniacode.armoniaskills.util;

import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.security.Key;
import java.util.Base64;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Configuration
public class JWTUtil {

    private static final String SECRET_KEY = "1e3e9a52af68343a63521fd8b6825584fc9dbe634ac81060f5cfa0b3b526daee"; //Generado con SHA256: ArmoniaSkills
    private static final long TOKEN_EXPIRATION_TIME = 7; // 7 days

    public String getJWTToken(String username) {
        List<GrantedAuthority> grantedAuthorities = AuthorityUtils
                .commaSeparatedStringToAuthorityList("ROLE_USER");

        byte[] decodedKey = Base64.getDecoder().decode(SECRET_KEY);
        Key key = new SecretKeySpec(decodedKey, 0, decodedKey.length, "HmacSHA256");

        String token = Jwts
                .builder()
                .id(UUID.randomUUID().toString())
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
}