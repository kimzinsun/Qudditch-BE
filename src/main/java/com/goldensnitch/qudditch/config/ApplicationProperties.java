package com.goldensnitch.qudditch.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.PropertySource;

import lombok.Data;

@Data
@Configuration
@ConfigurationProperties(prefix = "app")
public class ApplicationProperties {
    private String jwtSecret;
    private long  jwtExpirationInMs; // 타입을 long으로 변경합니다.

    // Lombok @Data 어노테이션은 getter와 setter를 자동으로 생성함
}
