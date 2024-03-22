package com.goldensnitch.qudditch.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.goldensnitch.qudditch.dto.UserAdmin;
import com.goldensnitch.qudditch.dto.UserCustomer;
import com.goldensnitch.qudditch.dto.UserStore;
import com.goldensnitch.qudditch.repository.UserAdminRepository;
import com.goldensnitch.qudditch.repository.UserCustomerRepository;
import com.goldensnitch.qudditch.repository.UserStoreRepository;

// CustomUserDetailsService 클래스는 Spring Security의 UserDetailsService 인터페이스를 구현하여
// 사용자 인증 정보를 불러오는 역할을 합니다.
@Service
public class CustomUserDetailsService implements UserDetailsService{
    
    // UserCustomerRepository의 인스턴스를 자동 주입한다.
    @Autowired
    private UserCustomerRepository userCustomerRepository;

    @Autowired
    private UserStoreRepository userStoreRepository;

    @Autowired
    private UserAdminRepository userAdminRepository; // 관리자 저장소 추가

    private static final Logger log = LoggerFactory.getLogger(CustomUserDetailsService.class);

    // 이메일을 사용하여 사용자를 검색하고, 해당 사용자의 인증 정보를 UserDetails 객체로 반환합니다.
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        log.info("Loading user by email: {}", email);

        UserCustomer userCustomer = userCustomerRepository.selectUserByEmail(email);
        UserStore userStore = userStoreRepository.findByEmail(email);
        UserAdmin userAdmin = userAdminRepository.findByEmail(email); // 관리자 조회 추가

        if (userCustomer != null) {
            return new org.springframework.security.core.userdetails.User(
                    userCustomer.getEmail(),
                    userCustomer.getPassword(),
                    AuthorityUtils.createAuthorityList("ROLE_CUSTOMER"));
        } else if (userStore != null) {
            return new org.springframework.security.core.userdetails.User(
                    userStore.getEmail(),
                    userStore.getPassword(),
                    AuthorityUtils.createAuthorityList("ROLE_STORE"));
        } else if (userAdmin != null) {
            return new org.springframework.security.core.userdetails.User(
                    userAdmin.getEmail(),
                    userAdmin.getPassword(),
                    AuthorityUtils.createAuthorityList("ROLE_ADMIN")); // 관리자 권한 추가
        }

        throw new UsernameNotFoundException("User not found with email: " + email);

    }

}