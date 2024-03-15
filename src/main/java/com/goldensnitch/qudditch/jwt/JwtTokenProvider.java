/* 
package com.goldensnitch.qudditch.jwt;

import io.jsonwebtoken.Jwts; // 올바른 임포트 문
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

    @SuppressWarnings("deprecation")
    public String generateToken(Authentication authentication) {
        String username = authentication.getName();
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + jwtExpirationInMs);
    
        return Jwts.builder()
                .subject(username)
                .issuedAt(now)
                .expiration(expiryDate) // Deprecated 메서드 대신 사용
                .signWith(secretKey, SignatureAlgorithm.HS256) // 알고리즘 지정을 생략하고, 키 자체에서 알고리즘 유추
                .compact();
    }

    public boolean validateToken(String authToken) {
        try {
            Jwts.parser()
                .decryptWith(secretKey)
                .build()
                .parseSignedClaims(authToken);
            return true;
            // Jwts.builder()
            //     .setSigningKey(secretKey)
            //     .build()
            //     .parseClaimsJws(authToken);
            // return true;
        } catch (ExpiredJwtException e)  {
            // 토큰이 만료된 경우
        } catch (UnsupportedJwtException e) {
            // 지원하지 않는 JWT인 경우
        } catch(JwtException e) {
            // 그 외의 JWT 관련 예외 처리
        }
        return false;
    }

    // jwtTokenProvvider.java 내에 리프레시 토큰 생성 및 검증 로직 추가

    public String generateRefreshToken(Authentication authentication, boolean isApp) {
        long expirationTime = isApp ? 7776000000L : 86400000L;  // 앱은 3개월(90일), 웹은 24시간

        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + expirationTime);

        //리프레시 토큰 생성
        return Jwts.builder()
                .setSubject(authentication.getName())
                .setIssuedAt(now)
                .setExpiration(expiryDate)
                .signWith(secretKey)
                .compact();
    }

    public boolean validateRefreshToken(String token) {
        try {
            Jwts.parser()
                .decryptWith(secretKey)
                .build()
                .parseSignedClaims(token);
            return true;
        } catch (ExpiredJwtException e) {
            // 리프레시 토큰이 만료된 경우 처리
        } catch (JwtException e) {
            //리프레시 토큰이 유효하지 않은 경우 처리
        }
        return false;
    }

    public String getUsernameFromJWT(String token) {
        Claims claims = Jwts.parser()
                .decryptWith(secretKey)
                .build()
                .parseSignedClaims(token)
                .getPayload();

        return claims.getSubject();
    }

    
}
*/
package com.goldensnitch.qudditch.jwt;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;
import com.goldensnitch.qudditch.config.ApplicationProperties;
import org.springframework.security.core.Authentication;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import javax.crypto.SecretKey;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Component
public class JwtTokenProvider {

    // Logger 인스턴스 생성
    private static final Logger logger = LoggerFactory.getLogger(JwtTokenProvider.class);
    private final SecretKey secretKey;
    private final long jwtExpirationInMs;

    // 생성자에서는 애플리케이션의 설정을 기반으로 SecreKey를 초기화합니다.
    public JwtTokenProvider(ApplicationProperties properties) {
        this.secretKey = Keys.hmacShaKeyFor(properties.getJwtSecret().getBytes(StandardCharsets.UTF_8));
        this.jwtExpirationInMs = properties.getJwtExpirationInMs();
    }

    // 토큰 생성 메소드
    public String generateToken(Authentication authentication) {
        // 토큰 생성 로직
        String username = authentication.getName();
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + jwtExpirationInMs);

        return Jwts.builder()
                .claim("sub", username)
                .claim("iat", now)
                .claim("exp", expiryDate)
                .signWith(secretKey, SignatureAlgorithm.HS256)
                .compact();
    }

    // 토큰 유효성 검증 메소드
    public boolean validateToken(String authToken) {
        try {
            // 토큰 파싱 시도
            Jwts.parserBuilder()
                .setSigningKey(secretKey)
                .build()
                .parseClaimsJws(authToken);
            // 성공적으로 파싱되면 true 반환
            return true;
        } catch (ExpiredJwtException e) {
            // 토큰이 만료된 경우
            logger.info("Expired JWT token.");
        } catch (UnsupportedJwtException e) {
            // 지원되지 않는 토큰 형식인 경우
            logger.info("Unsupported JWT token.");
        } catch (MalformedJwtException e) {
            // 구조적으로 잘못된 JWT인 경우
            logger.info("Invalid JWT token.");
        // 기존의 SignatureException 대신 SecurityException 사용
        } catch (SecurityException e) {
            // JWT 서명이 유효하지 않은 경우
            logger.info("Invalid JWT signature.");
        } catch (IllegalArgumentException e) {
            // 부적절한 인자가 전달된 경우
            logger.info("JWT token compact of handler are invalid.");
        }
        // 위의 어떤 경우에도 해당하지 않으면 false 반환
        return false;
    }

    // 나머지 메소드들...
}
