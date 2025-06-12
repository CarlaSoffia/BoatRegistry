package com.example.BoatRegistry.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Base64;
import java.util.Date;

@Component
public class JwtHelper {
    @Value("${jwt.secret}")
    private String base64Secret;
    @Value("${jwt.expirationDurationMin}")
    private int expirationDurationMin;

    public String generateToken(String email) {
        var now = Instant.now();
        var key = getSecretKey();
        return Jwts.builder()
                .subject(email)
                .issuedAt(Date.from(now))
                .expiration(Date.from(now.plus(expirationDurationMin, ChronoUnit.MINUTES)))
                .signWith(key)
                .compact();
    }

    public String getEmail(String token) {
        return getTokenBody(token).getSubject();
    }

    public Boolean validateToken(String token, UserDetails userDetails) {
        if(isTokenExpired(token)){
            return false;
        }
        var email = getEmail(token);
        return email.equals(userDetails.getUsername());
    }

    private Claims getTokenBody(String token) {
        try {
            var key = getSecretKey();
            return Jwts.parser()
                    .verifyWith(key)
                    .build()
                    .parseSignedClaims(token)
                    .getPayload();
        } catch (SignatureException | ExpiredJwtException e) {
            throw new AccessDeniedException("Access denied: " + e.getMessage());
        }
    }

    private boolean isTokenExpired(String token) {
        Claims claims = getTokenBody(token);
        return claims.getExpiration().before(new Date());
    }

    public SecretKey getSecretKey() {
        byte[] decodedKey = Base64.getDecoder().decode(base64Secret);
        return Keys.hmacShaKeyFor(decodedKey);
    }
}
