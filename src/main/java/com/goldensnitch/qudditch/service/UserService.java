package com.goldensnitch.qudditch.service;

import com.goldensnitch.qudditch.model.UserCustomer;
import com.goldensnitch.qudditch.repository.UserCustomerRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;


import java.util.UUID;

// UserService 클래스는 사용자 관련 비즈니스 로직을 처리합니다.
@Slf4j
@Service
public class UserService {

    private final UserCustomerRepository userCustomerRepository;
    private final PasswordEncoder passwordEncoder;
    private final EmailService emailService;

    public UserService(UserCustomerRepository userCustomerRepository,
                    PasswordEncoder passwordEncoder,
                    EmailService emailService) {
        this.userCustomerRepository = userCustomerRepository;
        this.passwordEncoder = passwordEncoder;
        this.emailService = emailService;
    }
    public void registerUserCustomer(UserCustomer userCustomer) {
    try {
        String encodedPassword = passwordEncoder.encode(userCustomer.getPassword());
        userCustomer.setPassword(encodedPassword);
        String verificationCode = UUID.randomUUID().toString();
        userCustomer.setVerificationCode(verificationCode);
        userCustomerRepository.insertUserCustomer(userCustomer);
        emailService.sendVerificationEmail(userCustomer.getEmail(), verificationCode);
        log.info("User registered successfully: {}", userCustomer.getEmail());
    } catch (Exception e) {
        log.error("Failed to register user: {}", userCustomer.getEmail(), e);
        throw new RuntimeException("Failed to register user", e);
    }
}

    // UserStore 회원가입 로직 및 이메일 인증 코드 검증 메서드는 생략
}
