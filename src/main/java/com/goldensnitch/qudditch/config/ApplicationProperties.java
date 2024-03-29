package com.goldensnitch.qudditch.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import lombok.Data;

@Data
@Configuration
@ConfigurationProperties(prefix = "app")
public class ApplicationProperties {
    private String jwtSecret;
    private long jwtExpirationInMs;
    private long refreshTokenExpirationInMsWeb; // 웹용 리프레시 토큰 만료 시간
    private long refreshTokenExpirationInMsApp; // 앱용 리프레시 토큰 만료 시간

    // Lombok @Data 어노테이션은 getter와 setter를 자동으로 생성함
}
