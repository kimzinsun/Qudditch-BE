package com.goldensnitch.qudditch.service;

import com.goldensnitch.qudditch.model.UserCustomer;
import com.goldensnitch.qudditch.model.UserStore;
import com.goldensnitch.qudditch.repository.UserCustomerRepository;
import com.goldensnitch.qudditch.repository.UserStoreRepository;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    
    @Autowired
    private UserCustomerRepository userCustomerRepository;

    @SuppressWarnings("unused")
    @Autowired
    private UserStoreRepository userStoreRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private EmailService emailService;

    // UserCustomer 회원가입 로직
    public void registerUserCustomer(UserCustomer userCustomer) {
        String encodedPassword = passwordEncoder.encode(userCustomer.getPassword());
        userCustomer.setPassword(encodedPassword);
        
        String verificationCode = UUID.randomUUID().toString();
        userCustomer.setVerificationCode(verificationCode);
    
        userCustomerRepository.save(userCustomer);
        
        emailService.sendVerificationEmail(userCustomer.getEmail());
    }

    // UserStore 회원가입 로직 (예시, 구체적인 구현은 생략)
    public void registerUserStore(UserStore userStore) {
        // UserStore 회원가입 로직을 여기에 구현합니다.
        // 예를 들어, 비밀번호 해시화, 이메일 인증 코드 생성 및 저장, 이메일 인증 메일 전송 등
    }
}
