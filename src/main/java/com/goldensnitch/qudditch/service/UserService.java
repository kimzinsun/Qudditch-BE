package com.goldensnitch.qudditch.service;

import com.goldensnitch.qudditch.model.UserCustomer;
import com.goldensnitch.qudditch.model.UserStore;
import com.goldensnitch.qudditch.repository.UserCustomerRepository;
import com.goldensnitch.qudditch.repository.UserStoreRepository;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

// UserService 클래스는 사용자 관련 비즈니스 로직을 처리합니다.
@Service
public class UserService {
    
    // 필요한 의존성 컴포넌트들을 자동 주입합니다.
    @Autowired
    private UserCustomerRepository userCustomerRepository;

    @Autowired
    private UserStoreRepository userStoreRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private EmailService emailService;  // SendGrid 이메일 서비스


    
    // UserCustomer 회원가입 로직
    public void registerUserCustomer(UserCustomer userCustomer) {
        // 비밀번호 암호화 및 저장
        String encodePassword = passwordEncoder.encode(userCustomer.getPassword());
        userCustomer.setPassword(encodePassword);

        // 이메일 인증 코드 생성 및 저장
        String verificationCode = UUID.randomUUID().toString();
        userCustomer.setVerificationCode(verificationCode);
        
        // UserCustomer 정보를 데이터베이스에 저장
        userCustomerRepository.insertUserCustomer(userCustomer);
        
        // SendGrid와 네이버 메일 서비스를 모두 사용하여 이메일 발송
        emailService.sendVerificationEmail(userCustomer.getEmail(), verificationCode);
        
    }

    // UserStore 회원가입 로직 (예시, 구체적인 구현은 생략)
    public void registerUserStore(UserStore userStore) {

        // 비밀번호 암호화 및 저장
        String encodedPassword = passwordEncoder.encode(userStore.getPassword());
        userStore.setPassword(encodedPassword);

        // 이메일 인증 코드 생성 및 저장
        String verificationCode = UUID.randomUUID().toString();
        // userStore.setVerificationCode(verificationCode); // UserStore 모델에 인증 코드 필드가 있는 경우

        // UserStore 정보를 데이터베이스에 저장
        userStoreRepository.insertUserStore(userStore);

        // 이메일 인증 메일 전송
        emailService.sendVerificationEmail(userStore.getEmail(), verificationCode);
    }

    // 이메일 인증 코드 검증 메서드
    public boolean verifyEmail(String verificationCode) {
        // 데이터베이스에서 인증 코드로 사용자 검색
    UserCustomer userCustomer = userCustomerRepository.findByVerificationCode(verificationCode);
    if (userCustomer != null) {
        // 사용자 상태를 '인증됨'으로 업데이트
        userCustomer.setState(1); // 예를 들어 1이 '인증됨' 상태를 나타낸다고 가정
        userCustomerRepository.updateUserCustomer(userCustomer);
        return true;
    }
    return false;
}

    // 추가 메서드...
}
