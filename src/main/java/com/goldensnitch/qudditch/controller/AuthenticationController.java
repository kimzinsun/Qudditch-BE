package com.goldensnitch.qudditch.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import com.goldensnitch.qudditch.jwt.JwtTokenProvider; // 가정한 패키지 경로, 실제 경로에 맞게 수정 필요
import com.goldensnitch.qudditch.dto.LoginRequest; // 실제 경로에 맞게 수정 필요
import com.goldensnitch.qudditch.dto.SocialLoginDto;
import com.goldensnitch.qudditch.dto.AuthResponse; // 실제 경로에 맞게 수정 필요

@RestController
public class AuthenticationController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtTokenProvider jwtTokenProvider; // JWT 토큰 제공자 의존성 주입

    @PostMapping("/login")
    public ResponseEntity<?> authenticateUser(@RequestBody LoginRequest loginRequest) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword()));
        SecurityContextHolder.getContext().setAuthentication(authentication);

        String token = jwtTokenProvider.generateToken(authentication);

        return ResponseEntity.ok(new AuthResponse(token));
    }

    // 소셜 로그인 및 이메일 인증 통합을 위한 컨트롤러 메소드 추가
    @PostMapping("/social-login/naver")
    public ResponseEntity<?> socialLogin(@RequestBody SocialLoginDto  socialLoginDto) {
        // 네이버 소셜 로그인 후 받은 정보를 처리하는 로직을 구현
        // 이 때, 필요한 정보를 DTO로부터 받아오고 처리 결과를 반환합니다.
        // 예시로 AuthResponse를 사용하여 토큰과 함께 응답
        String token = "가상의 토큰"; // 이 부분은 실제 로직에 따라 생성된 토큰으로 대체해야 합니다.
        return ResponseEntity.ok(new AuthResponse(token));
    }

    @GetMapping("/loginSuccess")
    public String loginSuccess(@AuthenticationPrincipal OAuth2User user) {
        // 로그인 성공 후 사용자 정보 처리
        // 'user' 객체에는 네이버로부터 받은 사용자 정보가 들어 있습니다.
        return "로그인에 성공했습니다.";
    }

    @GetMapping("/loginFailure")
    public String loginFailure() {
        // 로그인 실패 처리
        return "로그인에 실패했습니다.";
    }
    
}
