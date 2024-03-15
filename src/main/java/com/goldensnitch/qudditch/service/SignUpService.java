package com.goldensnitch.qudditch.service;

import com.goldensnitch.qudditch.model.UserCustomer;
import com.goldensnitch.qudditch.repository.UserCustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class SignUpService {

    @Autowired
    private UserCustomerRepository userCustomerRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    // 회원가입 로직
    public Integer signUp(UserCustomer user) {
        // 비밀번호를 암호화하여 저장합니다.
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        
        // 사용자 상태를 '미인증' 상태로 설정합니다.
        user.setState(0);
        
        // 저장된 사용자 객체를 반환합니다.
        return userCustomerRepository.insertUserCustomer(user);
    }
}
