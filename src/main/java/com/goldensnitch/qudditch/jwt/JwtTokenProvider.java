package com.goldensnitch.qudditch.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class JwtTokenProvider {

    @Value("${app.jwtSecret}")
    private String jwtSecret;

    @Value("${app.jwtExpirationInMs}")
    private int jwtExpirationInMs;


    public String generateToken(Authentication authentication) {
        String username = authentication.getName();
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + jwtExpirationInMs);

        return Jwts.builder()
                .setSubject(username)
                .setIssuedAt(new Date())
                .setExpiration(expiryDate)
                .signWith(SignatureAlgorithm.HS256, jwtSecret)
                .compact();
    }

    // 토큰에서 사용자 이름 가져오기
    /**
     * @param token
     * @return
     */
    public String getUsernameFromJWT(String token) {
        final Claims claims = ((JwtParser) Jwts.parserBuilder()
        .setSigningKey(jwtSecret))
                .parseClaimsJws(token)
                .getBody();

        return claims.getSubject();
    }

    // 토큰의 유효성 검사
    @SuppressWarnings("deprecation")
    public boolean validateToken(String authToken) {
        try {
            Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(authToken);
            return true;
        } catch (Exception e) {
            // 로그를 남기거나, 필요한 예외 처리를 수행
        }
        return false;
    }
}
