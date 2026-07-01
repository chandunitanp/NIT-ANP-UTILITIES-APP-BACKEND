package com.nit.noticeboard.service;

import java.nio.charset.StandardCharsets;
import java.util.Date;

import jakarta.annotation.PostConstruct;
import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.nit.noticeboard.model.User;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

@Service
public class JwtService {

    @Value("${jwt.secret}")
    private String secret;

    @Value("${jwt.expiration}")
    private long expirationMs;

    private SecretKey secretKey;

    // ================= LOAD SECRET =================
    @PostConstruct
    public void init() {

        // Convert string secret to SecretKey
        secretKey = Keys.hmacShaKeyFor(
                secret.getBytes(StandardCharsets.UTF_8)
        );

    }

    // ================= RETURN SIGNING KEY =================
    public SecretKey signingKey() {
        return secretKey;
    }

    // ================= GENERATE JWT =================
    public String generateToken(User user) {

        return Jwts.builder()
                .setSubject(user.getEmail())
                .claim("role", user.getRole().name())
                .claim("department", user.getDepartmentName())
                .setIssuedAt(new Date())
                .setExpiration(
                        new Date(
                                System.currentTimeMillis()
                                        + expirationMs
                        )
                )
                .signWith(secretKey)
                .compact();
    }
}