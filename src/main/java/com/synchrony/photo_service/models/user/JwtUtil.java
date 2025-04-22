package com.synchrony.photo_service.models.user;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import java.security.Key;
import java.util.Date;

public class JwtUtil {
    public static final Key SECRET_KEY = Keys.secretKeyFor(SignatureAlgorithm.HS256);
    private static final Integer TEN_HOURS = 1000 * 60 * 60 * 10;
    public static String generateToken(String username) {
        return Jwts.builder()
            .setSubject(username)
            .setIssuedAt(new Date())
            .setExpiration(new Date(System.currentTimeMillis() + TEN_HOURS))
            .signWith(SECRET_KEY) // Use the secure key
            .compact();
    }
}