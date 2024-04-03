package com.goldensnitch.qudditch.service;

import com.goldensnitch.qudditch.dto.SocialLoginDto;
import com.goldensnitch.qudditch.dto.UserAdmin;
import com.goldensnitch.qudditch.dto.UserCustomer;
import com.goldensnitch.qudditch.dto.UserStore;
import com.goldensnitch.qudditch.mapper.UserAdminMapper;
import com.goldensnitch.qudditch.mapper.UserCustomerMapper;
import com.goldensnitch.qudditch.mapper.UserStoreMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

@Service
public class UserService {
    private final UserCustomerMapper userCustomerMapper;
    private final UserStoreMapper userStoreMapper;
    private final UserAdminMapper userAdminMapper;
    private final PasswordEncoder passwordEncoder;
    private final EmailService emailService;
    private static final Logger log = LoggerFactory.getLogger(UserService.class);

    @Autowired
    public UserService(UserCustomerMapper userCustomerMapper, UserStoreMapper userStoreMapper, UserAdminMapper userAdminMapper, PasswordEncoder passwordEncoder, EmailService emailService) {
        this.userCustomerMapper = userCustomerMapper;
        this.userStoreMapper = userStoreMapper;
        this.userAdminMapper = userAdminMapper;
        this.passwordEncoder = passwordEncoder;
        this.emailService = emailService;
    }

    // 일반유저 회원가입 로직
    public ResponseEntity<String> registerUserCustomer(UserCustomer userCustomer) {
        try {
            // 입력된 이메일이 이미 사용중인지 검사합니다.
            if (userCustomerMapper.findByEmail(userCustomer.getEmail()) != null) {
                log.error("이미 존재하는 이메일입니다: {}", userCustomer.getEmail());
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("이미 존재하는 이메일입니다.");
            }

            // 비밀번호를 암호화하고 신규 사용자 속성을 설정합니다.
            userCustomer.setPassword(passwordEncoder.encode(userCustomer.getPassword()));
            userCustomer.setState(0); // 미인증 사용자 상태로 가정합니다.
            userCustomer.setVerificationCode(UUID.randomUUID().toString()); // 인증 코드를 생성합니다.

            // 사용자 정보를 데이터베이스에 저장합니다.
            userCustomerMapper.insertUserCustomer(userCustomer);
            log.info("고객 등록이 성공적으로 완료되었습니다: {}", userCustomer.getEmail());
            return ResponseEntity.ok("고객 등록이 성공적으로 완료되었습니다.");
        } catch (DataAccessException e) {
            log.error("데이터베이스 접근 중 오류가 발생했습니다.", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("데이터베이스 접근 중 오류가 발생했습니다.");
        } catch (Exception e) {
            log.error("알 수 없는 오류가 발생했습니다.", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("알 수 없는 오류가 발생했습니다.");
        }
    }

    // 점주 회원가입 로직
    public ResponseEntity<String> registerUserStore(UserStore userStore) {
        try {
            if (userStoreMapper.findByEmail(userStore.getEmail()) != null) {
                log.error("이미 존재하는 이메일입니다: {}", userStore.getEmail());
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("이미 존재하는 이메일입니다.");
            }

            // 비밀번호 암호화
            userStore.setPassword(passwordEncoder.encode(userStore.getPassword()));
            userStore.setState(0); // 기본 상태로 설정

            // 순차적으로 store_id 설정
//            Integer maxStoreId = userStoreMapper.findMaxStoreId();
//            int nextStoreId = (maxStoreId == null) ? 1 : maxStoreId + 1;
//            userStore.setStoreId(nextStoreId);

            // 데이터베이스에 사용자 정보 저장
            userStoreMapper.insertUserStore(userStore);
            log.info("점포 등록에 성공했습니다: {}", userStore.getEmail());
            return ResponseEntity.ok("점포 등록에 성공했습니다.");
        } catch (DataAccessException e) {
            log.error("데이터베이스 접근 중 오류가 발생했습니다.", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("데이터베이스 접근 중 오류가 발생했습니다.");
        } catch (Exception e) {
            log.error("점포 등록 중 알 수 없는 오류가 발생했습니다.", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("점포 등록 중 알 수 없는 문제가 발생했습니다.");
        }
    }

    // 관리자 회원가입 로직
    public ResponseEntity<String> registerUserAdmin(UserAdmin userAdmin) {
        try {
            // 이메일 중복 검사
            if (userAdminMapper.findByEmail(userAdmin.getEmail()) != null) {
                log.error("이미 존재하는 이메일입니다: {}", userAdmin.getEmail());
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("이미 존재하는 이메일입니다.");
            }

            // 비밀번호 암호화
            userAdmin.setPassword(passwordEncoder.encode(userAdmin.getPassword()));
            // 추가적으로 설정해야 할 관리자 속성이 있다면 여기에 코드 추가

            // 관리자 정보 데이터베이스에 저장
            userAdminMapper.insertUserAdmin(userAdmin);
            log.info("관리자 등록에 성공했습니다: {}", userAdmin.getEmail());
            return ResponseEntity.ok("관리자 등록에 성공했습니다.");
        } catch (Exception e) {
            log.error("관리자 등록에 실패했습니다: {}", userAdmin.getEmail(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("관리자 등록에 실패했습니다");
        }
    }

    public UserDetails processUserIntegration(String provider, SocialLoginDto socialLoginDto) {
        String email = socialLoginDto.getEmail();
        UserCustomer userCustomer = userCustomerMapper.selectUserByEmail(email);
        ExtendedUserDetails userDetails;

        if (userCustomer != null) {
            log.info("기존 사용자와 소셜 계정 통합: {}", email);
            // 여기에 기존 사용자에 대한 업데이트 로직을 추가합니다. 예를 들면:
            // userCustomer.setSomeField(socialLoginDto.getSomeField());
            userCustomerMapper.updateUserCustomer(userCustomer); // 데이터베이스 업데이트
            userDetails = new ExtendedUserDetails(
                userCustomer.getEmail(),
                userCustomer.getPassword(),
                AuthorityUtils.createAuthorityList("ROLE_USER"),
                userCustomer.getId(),
                userCustomer.getName(),
                userCustomer.getEmail(),
                true, true, true, userCustomer.getState() != 2
            );
        } else {
            log.info("새 소셜 사용자 등록: {}", email);
            UserCustomer newUserCustomer = new UserCustomer();
            // socialLoginDto에서 받은 정보로 newUserCustomer 객체 설정
            newUserCustomer.setEmail(email);
            // ... [다른 필드 설정] ...
            userCustomerMapper.insertUserCustomer(newUserCustomer); // 데이터베이스에 삽입
            userDetails = new ExtendedUserDetails(
                newUserCustomer.getEmail(),
                newUserCustomer.getPassword(),
                AuthorityUtils.createAuthorityList("ROLE_USER"),
                newUserCustomer.getId(),
                newUserCustomer.getName(),
                newUserCustomer.getEmail(),
                true, true, true, newUserCustomer.getState() != 2
            );
        }

        return userDetails; // 처리된 UserDetails 객체 반환
    }
    
    public List<UserStore> searchStoresByName(String name) {
        return userStoreMapper.searchByName(name);
    }
    //  ... 기타 메서드

     // 아이디 찾기 서비스 메서드
    public String findUsernameByName(String name) {
        UserCustomer user = userCustomerMapper.selectUserByName(name);
        if (user != null) {
            return user.getEmail(); // 사용자의 이메일을 반환합니다.
        } else {
            throw new UsernameNotFoundException("User not found with name: " + name);
        }
    }

    // 비밀번호 찾기 서비스 메서드
    public void resetPassword(String email) {
        UserCustomer user = userCustomerMapper.selectUserByEmail(email);
        if (user != null) {
            String temporaryPassword = UUID.randomUUID().toString().replace("-", "").substring(0, 8); // 8자리 임시 비밀번호 생성
            user.setPassword(passwordEncoder.encode(temporaryPassword)); // 임시 비밀번호를 암호화하여 설정
            userCustomerMapper.updateUserCustomer(user); // 데이터베이스 업데이트
            
            // 임시 비밀번호를 사용자 이메일로 전송
            try {
                emailService.sendPasswordResetEmail(email, temporaryPassword);
            } catch (IOException e) {
                log.error("Failed to send password reset email", e);
                throw new EmailSendingException("Failed to send password reset email");
            }
        } else {
            throw new UsernameNotFoundException("User not found with email: " + email);
        }
    }

    // 기타 메서드...
}
