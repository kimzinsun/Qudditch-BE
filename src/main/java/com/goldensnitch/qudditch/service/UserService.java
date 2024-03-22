package com.goldensnitch.qudditch.service;

import java.io.IOException;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.goldensnitch.qudditch.dto.UserCustomer;
import com.goldensnitch.qudditch.repository.UserCustomerRepository;
@Service
public class UserService {

    private final UserCustomerRepository userCustomerRepository;
    private final PasswordEncoder passwordEncoder;
    private final EmailService emailService;

    private static final Logger log = LoggerFactory.getLogger(UserService.class);

    @Autowired
    public UserService(UserCustomerRepository userCustomerRepository,
                    PasswordEncoder passwordEncoder,
                    EmailService emailService) {
        this.userCustomerRepository = userCustomerRepository;
        this.passwordEncoder = passwordEncoder;
        this.emailService = emailService;
    }

    public void registerUserCustomer(UserCustomer userCustomer) {
        try {
            // 비밀번호 암호화
            String encodedPassword = passwordEncoder.encode(userCustomer.getPassword());
            userCustomer.setPassword(encodedPassword);

            // 인증 코드 생성 및 설정
            String verificationCode = UUID.randomUUID().toString();
            userCustomer.setVerificationCode(verificationCode);

            // 사용자 상태를 '미인증'(0)으로 설정
            userCustomer.setState(0);

            // 사용자 정보 저장
            userCustomerRepository.insertUserCustomer(userCustomer);

            // 인증 이메일 전송
            sendVerificationEmail(userCustomer);

            log.info("User registered successfully: {}", userCustomer.getEmail());
        } catch (Exception e) {
            log.error("Failed to register user: {}", userCustomer.getEmail(), e);
            throw new RuntimeException("Failed to register user", e);
        }
    }

    private void sendVerificationEmail(UserCustomer userCustomer) {
        String verificationCode = userCustomer.getVerificationCode();
        String verificationUrl = "http://yourdomain.com/verify?code=" + verificationCode;
        
        try {
            emailService.sendVerificationEmail(userCustomer.getEmail(), verificationUrl);
        } catch (IOException e) {
            log.error("Failed to send verification email to: {}", userCustomer.getEmail(), e);
            throw new EmailSendingException("Failed to send verification email");
        }
    }
}
