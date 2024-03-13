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
        // 비밀번호 암호화 및 저장
        // ...

        String encodedPassword = passwordEncoder.encode(userCustomer.getPassword());
        userCustomer.setPassword(encodedPassword);

        // 이메일 인증 코드 생성 및 저장
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

    // 이메일 인증 코드 검증 메서드
    public boolean verifyEmail(String verificationCode) {
        // 데이터베이스에서 인증 코드를 검색하여 검증하는 로직 구현
        // ...
        return true; // 또는 false, 검증 결과에 따라
    }

    // 추가 메서드...
}
