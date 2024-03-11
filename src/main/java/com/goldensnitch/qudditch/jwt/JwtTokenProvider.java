package com.goldensnitch.qudditch.jwt;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;

import com.goldensnitch.qudditch.config.ApplicationProperties;

import org.springframework.security.core.Authentication;

import java.nio.charset.StandardCharsets;
import java.util.Date;
import javax.crypto.SecretKey;

@Component
public class JwtTokenProvider {

    private final SecretKey secretKey;
    private final long jwtExpirationInMs;

    public JwtTokenProvider(ApplicationProperties properties) {
        this.secretKey = Keys.hmacShaKeyFor(properties.getJwtSecret().getBytes(StandardCharsets.UTF_8));
        this.jwtExpirationInMs = properties.getJwtExpirationInMs();
    }

    public String generateToken(Authentication authentication) {
        String username = authentication.getName();
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + jwtExpirationInMs);

        return Jwts.parserBuilder()
                .subject(username)
                .issuedAt(now)
                .expiration(expiryDate) // Deprecated 메서드 대신 사용
                .signWith(secretKey, SignatureAlgorithm.HS256) // 알고리즘 지정을 생략하고, 키 자체에서 알고리즘 유추
                .compact();
    }

    public String getUsernameFromJWT(String token) {
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(secretKey)
                .build()
                .parseClaimsJws(token)
                .getBody();

        return claims.getSubject();
    }

    public boolean validateToken(String authToken) {
        try {
            Jwts.parserBuilder()
                .setSigningKey(secretKey)
                .build()
                .parseClaimsJws(authToken);
            return true;
        } catch (JwtException e) {
            // 로그 남기기나, 다른 예외 처리를 수행
        }
        return false;
    }
}