// package com.goldensnitch.qudditch.jwt;

// import io.jsonwebtoken.*;
// import io.jsonwebtoken.security.Keys;
// import org.springframework.stereotype.Component;
// import com.goldensnitch.qudditch.config.ApplicationProperties;
// import org.springframework.security.core.Authentication;
// import java.nio.charset.StandardCharsets;
// import java.util.Date;
// import javax.crypto.SecretKey;
// import org.slf4j.Logger;
// import org.slf4j.LoggerFactory;

// @Component
// public class JwtTokenProvider {

//     // Logger 인스턴스 생성
//     private static final Logger logger = LoggerFactory.getLogger(JwtTokenProvider.class);
//     private final SecretKey secretKey;
//     private final long jwtExpirationInMs;

//     // 생성자에서는 애플리케이션의 설정을 기반으로 SecreKey를 초기화합니다.
//     public JwtTokenProvider(ApplicationProperties properties) {
//         // 시크릿 키를 생성할 때 적절한 길이 확인
//         byte[] keyBytes = properties.getJwtSecret().getBytes(StandardCharsets.UTF_8);
//         System.out.println("Key length in bits: " + keyBytes.length * 8);
//         if (keyBytes.length < 32) {
//             throw new IllegalArgumentException("Key length must be at least 256 bits.");
//         }

//         // this.secretKey = Keys.hmacShaKeyFor(properties.getJwtSecret().getBytes(StandardCharsets.UTF_8));
//         this.secretKey = Keys.hmacShaKeyFor(keyBytes);
//         this.jwtExpirationInMs = properties.getJwtExpirationInMs();
//     }

//     // 토큰 생성 메소드
//     public String generateToken(Authentication authentication) {
//         // 토큰 생성 로직
//         String username = authentication.getName();
//         Date now = new Date();
//         Date expiryDate = new Date(now.getTime() + jwtExpirationInMs);

//         return Jwts.builder()
//                 .setSubject(username)
//                 .setIssuedAt(now)
//                 .setExpiration(expiryDate)
//                 .signWith(secretKey, SignatureAlgorithm.HS256)
//                 .compact();
//     }

//     // 토큰 유효성 검증 메소드
//     public boolean validateToken(String authToken) {
//         try {
//             // 토큰 파싱 시도
//             Jwts.parserBuilder()
//                     .setSigningKey(secretKey)
//                     .build()
//                     .parseClaimsJws(authToken);
//             // 성공적으로 파싱되면 true 반환
//             return true;
//         } catch (ExpiredJwtException e) {
//             // 토큰이 만료된 경우
//             logger.info("Expired JWT token.");
//         } catch (UnsupportedJwtException e) {
//             // 지원되지 않는 토큰 형식인 경우
//             logger.info("Unsupported JWT token.");
//         } catch (MalformedJwtException e) {
//             // 구조적으로 잘못된 JWT인 경우
//             logger.info("Invalid JWT token.");
//             // 기존의 SignatureException 대신 SecurityException 사용
//         } catch (SecurityException e) {
//             // JWT 서명이 유효하지 않은 경우
//             logger.info("Invalid JWT signature.");
//         } catch (IllegalArgumentException e) {
//             // 부적절한 인자가 전달된 경우
//             logger.info("JWT token compact of handler are invalid.");
//         }
//         // 위의 어떤 경우에도 해당하지 않으면 false 반환
//         return false;
//     }

//     // 나머지 메소드들...
// }
package com.goldensnitch.qudditch.jwt;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;
import com.goldensnitch.qudditch.config.ApplicationProperties;

import java.nio.charset.StandardCharsets;
import java.util.Date;
import javax.crypto.SecretKey;


@Component
public class JwtTokenProvider {


    private static final Logger logger = LoggerFactory.getLogger(JwtTokenProvider.class);
    private final SecretKey secretKey;
    private final long jwtExpirationInMs;



    public JwtTokenProvider(ApplicationProperties properties) {

        byte[] keyBytes = properties.getJwtSecret().getBytes(StandardCharsets.UTF_8);
        if (keyBytes.length * 8 < 256) { // 비트 단위로 계산
            logger.error("Key length is less than 256 bits.");
            throw new IllegalArgumentException("Key length must be at least 256 bits.");
        }


        this.secretKey = Keys.hmacShaKeyFor(keyBytes);
        this.jwtExpirationInMs = properties.getJwtExpirationInMs();
    }



    public String generateToken(Authentication authentication) {
        // 토큰 생성 로직 수정
        String username = authentication.getName();
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + jwtExpirationInMs);

        return Jwts.builder()
                .subject(username)
                .issuedAt(now)
                .expiration(expiryDate)
                .signWith(secretKey, Jwts.SIG.HS256)
                .compact();
    }


    public boolean validateToken(String authToken) {
        try {

            Jwts.parser()
                .verifyWith(secretKey)
                .build()
                .parseSignedClaims(authToken); // 대체 찾기
            return true;
        } catch (ExpiredJwtException e) {
            logger.error("Expired JWT token.", e);
        } catch (UnsupportedJwtException e) {
            logger.error("Unsupported JWT token.", e);
        } catch (MalformedJwtException e) {
            logger.error("Invalid JWT token.", e);
        } catch (SecurityException e) {
            logger.error("Invalid JWT signature.", e);
        } catch (IllegalArgumentException e) {
            logger.error("JWT token compact of handler are invalid.", e);
        }

        return false;
    
    

    }
    public Claims extractClaims(String token) {
        return Jwts.parser()
                .decryptWith(secretKey)
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    
}
