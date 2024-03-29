package com.goldensnitch.qudditch.service;

import com.goldensnitch.qudditch.dto.UserAdmin;
import com.goldensnitch.qudditch.dto.UserCustomer;
import com.goldensnitch.qudditch.dto.UserStore;
import com.goldensnitch.qudditch.mapper.UserAdminMapper;
import com.goldensnitch.qudditch.mapper.UserCustomerMapper;
import com.goldensnitch.qudditch.mapper.UserStoreMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

// CustomUserDetailsService 클래스는 Spring Security의 UserDetailsService 인터페이스를 구현하여
// 사용자 인증 정보를 불러오는 역할을 합니다.
@Service
public class CustomUserDetailsService implements UserDetailsService {
    private final UserCustomerMapper userCustomerMapper;
    private final UserStoreMapper userStoreMapper;
    private final UserAdminMapper userAdminMapper; // 관리자 저장소 추가
    private static final Logger log = LoggerFactory.getLogger(CustomUserDetailsService.class);

    @Autowired
    public CustomUserDetailsService(UserCustomerMapper userCustomerMapper, UserStoreMapper userStoreMapper, UserAdminMapper userAdminMapper) {
        this.userCustomerMapper = userCustomerMapper;
        this.userStoreMapper = userStoreMapper;
        this.userAdminMapper = userAdminMapper; // 관리자 저장소 추가
    }

    // 이메일을 사용하여 사용자를 검색하고, 해당 사용자의 인증 정보를 UserDetails 객체로 반환합니다.
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        log.info("Loading user by email: {}", email);

        UserCustomer userCustomer = userCustomerMapper.selectUserByEmail(email);
        UserStore userStore = userStoreMapper.findByEmail(email);
        UserAdmin userAdmin = userAdminMapper.findByEmail(email); // 관리자 조회 추가

        if (userCustomer != null) {
            return new ExtendedUserDetails(
                userCustomer.getEmail(),
                userCustomer.getPassword(),
                AuthorityUtils.createAuthorityList("ROLE_CUSTOMER"),
                userCustomer.getId(),
                userCustomer.getName(), email);
        } else if (userStore != null) {
            return new ExtendedUserDetails(
                userStore.getEmail(),
                userStore.getPassword(),
                AuthorityUtils.createAuthorityList("ROLE_STORE"),
                userStore.getId(),
                userStore.getName(), email);
        } else if (userAdmin != null) {
            return new ExtendedUserDetails(
                userAdmin.getEmail(),
                userAdmin.getPassword(),
                AuthorityUtils.createAuthorityList("ROLE_ADMIN"),
                userAdmin.getId(),
                userAdmin.getName(), email);
        }

        throw new UsernameNotFoundException("User not found with email: " + email);
    }
}