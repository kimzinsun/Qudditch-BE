package com.goldensnitch.qudditch.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

@Service
public class AuthenticationService {
    private final AuthenticationManager authenticationManager;

    @Autowired
    public AuthenticationService(AuthenticationManager authenticationManager) {
        this.authenticationManager = authenticationManager;
    }

    // 로그인 로직
    public Authentication login(String email, String password) {
        try {
            // 인증 관리자를 사용하여 사용자의 인증을 시도합니다.
            // 로그인 성공 시 인증 정보를 반환합니다.
            return authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(email, password)
            );
        } catch (Exception e) {
            // 로그인 실패 시 예외를 잡아서 처리합니다.
            throw new AuthenticationServiceException("Failed to authenticate user", e);
        }
    }
}
