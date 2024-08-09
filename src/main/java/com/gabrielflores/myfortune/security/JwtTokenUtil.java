package com.gabrielflores.myfortune.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import java.time.Duration;
import java.util.Date;
import java.util.function.Function;
import javax.crypto.SecretKey;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

@Component
public class JwtTokenUtil {

    private static final long JWT_EXPIRATION = Duration.ofHours(1).toMillis();
    private static final int JWT_REFRESH_TOKEN_LENGTH = 32;
    private static final String JWT_REFRESH_TOKEN_CHARS = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789#@$?&!+=-";

    private final SecretKey JWT_SECRET_KEY;

    public JwtTokenUtil(@Value("${api.jwt.secret}") String jwtSecret) {
        JWT_SECRET_KEY = Keys.hmacShaKeyFor(jwtSecret.getBytes());
    }

    public String generateToken(Authentication authentication) {
        return generateToken(authentication.getName());
    }

    public String generateToken(UserDetails user) {
        return generateToken(user.getUsername());
    }

    public String generateToken(String username) {
        long currentTimestampInMillis = System.currentTimeMillis();
        String token = Jwts
                .builder()
                .signWith(JWT_SECRET_KEY)
                .claims()
                .subject(username)
                .issuedAt(new Date(currentTimestampInMillis))
                .expiration(new Date(currentTimestampInMillis + JWT_EXPIRATION))
                .and()
                .compact();
        return token;
    }

    public String generateRefreshToken() {
        StringBuilder result = new StringBuilder();
        for (int i = 0; i < JWT_REFRESH_TOKEN_LENGTH; i++) {
            int position = (int) Math.floor(Math.random() * JWT_REFRESH_TOKEN_CHARS.length());
            result.append(JWT_REFRESH_TOKEN_CHARS.charAt(position));
        }
        return result.toString();
    }

    public String getUsernameFromToken(String token) {
        return getClaimFromToken(token, Claims::getSubject);
    }

    public Date getExpirationDateFromToken(String token) {
        return getClaimFromToken(token, Claims::getExpiration);
    }

    public <T> T getClaimFromToken(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = getAllClaimsFromToken(token);
        return claimsResolver.apply(claims);
    }

    private Claims getAllClaimsFromToken(String token) {
        return Jwts
                .parser()
                .verifyWith(JWT_SECRET_KEY)
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }
}
