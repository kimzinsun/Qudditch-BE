package com.goldensnitch.qudditch.config;

import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfiguration;

import lombok.Data;

@Data
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    
    protected void configure(HttpSecurity http) throws Exception {
        http
            // ... 기타 설정 ...
            .oauth2Login()
                .defaultSuccessUrl("/loginSuccess")
                .failureUrl("/loginFailure")
                // ... 기타 설정 ...
    }
}
