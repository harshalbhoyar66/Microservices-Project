package com.microservices.auth.security;

import com.microservices.auth.dto.AuthRequest;
import com.microservices.auth.util.JwtUtil;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    private final JwtUtil jwtUtil;

    public AuthService(JwtUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

    public String login(AuthRequest request) {

        // 🔥 ADMIN
        if ("admin".equals(request.getUsername()) &&
                "admin".equals(request.getPassword())) {

            return jwtUtil.generateToken("admin", "ADMIN");
        }

        // 🔥 USER
        if ("user".equals(request.getUsername()) &&
                "user".equals(request.getPassword())) {

            return jwtUtil.generateToken("user", "USER");
        }

        throw new RuntimeException("Invalid credentials");
    }
}