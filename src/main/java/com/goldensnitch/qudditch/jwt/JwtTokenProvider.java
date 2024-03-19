// 토큰사용법
// 토큰 발급: 사용자가 로그인을 성공하면 서버는 JWT 토큰을 발급하고,
// 이 토큰은 클라이언트(예: 웹 브라우저나 모바일 앱)에 반환됩니다.

// 토큰 사용: 클라이언트는 서버에 요청을 보낼 때, 이 토큰을 HTTP 요청 헤더에 포함시켜야 합니다. 
// 일반적으로 Authorization 헤더에 Bearer 스키마를 사용하여 토큰을 추가합니다.
// 예를 들어, JWT 토큰이 abc123이라면, 요청 헤더에 다음과 같이 포함시킵니다:
// Authorization: Bearer abc123

// 토큰 검증: 서버는 요청을 받을 때마다 Authorization 헤더에서 토큰을 추출하고, 이 토큰의 유효
// 성을 검증합니다. 토큰이 유효하면 요청이 처리되고, 유효하지 않으면 에러를 반환합니다.
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
