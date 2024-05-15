package com.armoniacode.armoniaskills.config;

import com.armoniacode.armoniaskills.entity.User;
import com.armoniacode.armoniaskills.util.JWTUtil;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Optional;

public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private JWTUtil jwtUtil;

    public JwtAuthenticationFilter(JWTUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

    @Override
protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
        throws ServletException, IOException {
    String header = request.getHeader("Authorization");

    if (header != null && header.startsWith("Bearer ")) {
        String token = header.split(" ")[1].trim();
        if (jwtUtil.validateToken(token)) {
            Optional<User> optionalUser = jwtUtil.getUserFromToken(token);
            if (optionalUser.isPresent()) {
                User user = optionalUser.get();
                UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                        user.getUsername(), null, user.getAuthorities()
                );
                authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authenticationToken);
            }
        }
    }

    filterChain.doFilter(request, response);
}
}