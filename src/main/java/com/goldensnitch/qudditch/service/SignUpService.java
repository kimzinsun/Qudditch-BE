package com.goldensnitch.qudditch.service;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.goldensnitch.qudditch.dto.UserCustomer;
import com.goldensnitch.qudditch.mapper.UserCustomerMapper;

@Service
public class SignUpService {

    @Autowired
    private UserCustomerMapper userCustomerMapper;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private EmailService emailService;

    // 회원가입 로직
    public Integer signUp(UserCustomer user) {
        // 비밀번호를 암호화하여 저장합니다.
        user.setPassword(passwordEncoder.encode(user.getPassword()));

        // 사용자 상태를 '미인증' 상태로 설정합니다.
        user.setState(0);
        
        // 이메일 인증 코드 추가
        user.setVerificationCode(UUID.randomUUID().toString());

        // 사용자 정보를 데이터베이스에 저장
        userCustomerMapper.insertUserCustomer(user);
        
        // 이메일 인증 링크 전송 로직을 try-catch 블록 내에 포함
        try {
            String verificationLink = "http://localhost:3000/verify?code=" + user.getVerificationCode();
            emailService.sendVerificationEmail(user.getEmail(), verificationLink);
        } catch (Exception e) {
            // 예외 처리 로직(로그 기록, 사용자에게 알림)
            e.printStackTrace();
        }

        // 저장된 사용자 객체를 반환합니다.
        return user.getId();
    }
}
