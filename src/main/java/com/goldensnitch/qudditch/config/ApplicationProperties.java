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

    //네이버 쿨라우드 OCR 서비스와 연동을 위한 프로퍼티
    private String ocrApiUrl;   //OCR API URL
    private String ocrSecretKey; // OCR Secret Key
    // Lombok @Data 어노테이션은 getter와 setter를 자동으로 생성함 이건 무조건 dto에사용해서 편리성 증가시킴
}
