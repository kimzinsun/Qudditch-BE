package com.goldensnitch.qudditch.service;

import java.io.IOException;
import java.time.Duration;
import java.util.List;
import java.util.UUID;

import org.apache.commons.lang3.RandomStringUtils;
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

import com.goldensnitch.qudditch.dto.SocialLoginDto;
import com.goldensnitch.qudditch.dto.UserAdmin;
import com.goldensnitch.qudditch.dto.UserCustomer;
import com.goldensnitch.qudditch.dto.UserStore;
import com.goldensnitch.qudditch.mapper.UserAdminMapper;
import com.goldensnitch.qudditch.mapper.UserCustomerMapper;
import com.goldensnitch.qudditch.mapper.UserStoreMapper;
@Service
public class UserService {
    private final UserCustomerMapper userCustomerMapper;
    private final UserStoreMapper userStoreMapper;
    private final UserAdminMapper userAdminMapper;
    private final PasswordEncoder passwordEncoder;
    private final EmailService emailService;
    private final RedisService redisService;
    private static final String REDIS_KEY_EMAIL_PREFIX = "email-";

    private static final Logger log = LoggerFactory.getLogger(UserService.class);

    @Autowired
    public UserService(UserCustomerMapper userCustomerMapper, UserStoreMapper userStoreMapper, UserAdminMapper userAdminMapper, PasswordEncoder passwordEncoder, EmailService emailService, RedisService redisService) {
        this.userCustomerMapper = userCustomerMapper;
        this.userStoreMapper = userStoreMapper;
        this.userAdminMapper = userAdminMapper;
        this.passwordEncoder = passwordEncoder;
        this.emailService = emailService;
        this.redisService = redisService;
    }

     // 일반유저 회원가입 로직
    public ResponseEntity<?> registerUserCustomer(UserCustomer userCustomer) {
        UserCustomer existingUser = userCustomerMapper.findByEmail(userCustomer.getEmail());
        
        if (existingUser != null && existingUser.getState() == 1) {
            existingUser.setPassword(passwordEncoder.encode(userCustomer.getPassword()));
            existingUser.setName(userCustomer.getName());
            userCustomerMapper.updateUserCustomer(existingUser);
            return ResponseEntity.ok("회원가입이 완료되었습니다.");
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("이메일 인증이 완료되지 않았습니다.");
        }
    }
    // 기타 메서드들...

    // 이메일 인증 코드를 생성하고 이메일을 전송하는 메서드
    public void sendEmailVerification(String email) {
        String verificationCode = RandomStringUtils.randomAlphanumeric(6);
        userCustomerMapper.updateVerificationCode(email, verificationCode); // DB에 코드 저장
        try {
            emailService.sendVerificationEmail(email, verificationCode); // 이메일 전송
        } catch (IOException e) {
            log.error("이메일 전송 중 에러 발생", e);
            // 오류 처리 로직...
        }
    }

        
    // 이메일 인증 요청을 처리하는 메서드
    public ResponseEntity<?> requestVerification(String email) {
        UserCustomer user = userCustomerMapper.findByEmail(email);
        
        if (user != null) {
            if (user.getVerificationCode() != null && !user.getVerificationCode().isEmpty()) {
                return ResponseEntity.status(HttpStatus.CONFLICT).body("이미 인증 코드가 발송되었습니다.");
            }
            // 새 인증 코드 생성 및 저장
            String verificationCode = RandomStringUtils.randomAlphanumeric(6);
            user.setVerificationCode(verificationCode);
            userCustomerMapper.updateUserCustomer(user);
        } else {
            user = new UserCustomer();
            user.setEmail(email);
            user.setState(0); // 미인증 상태로 설정
            user.setVerificationCode(RandomStringUtils.randomAlphanumeric(6));
            userCustomerMapper.insertUserCustomer(user);
        }
        
        // 인증 이메일 전송
        try {
            emailService.sendVerificationEmail(email, user.getVerificationCode());
            return ResponseEntity.ok("인증 이메일을 발송하였습니다.");
        } catch (IOException e) {
            log.error("이메일 전송 중 에러 발생", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("이메일 전송 중 에러가 발생하였습니다.");
        }
    }
    



    // 인증 코드를 검증하는 메서드
public ResponseEntity<?> verifyAccount(String email, String code) {
    UserCustomer user = userCustomerMapper.findByEmail(email);

    if (user != null && code.equals(user.getVerificationCode())) {
        // 상태를 '인증됨'으로 변경하지만, 사용자를 삭제하지는 않습니다.
        user.setState(1);
        user.setVerificationCode(null);
        userCustomerMapper.updateUserCustomer(user);
        return ResponseEntity.ok("계정이 인증되었습니다.");
    } else {
        return ResponseEntity.badRequest().body("인증 코드가 일치하지 않습니다.");
    }
}

    // 사용자의 비밀번호와 이름을 업데이트하는 메서드
    public ResponseEntity<?> updateUserCredentials(Integer userId, String password, String name) {
        UserCustomer user = userCustomerMapper.selectUserById(userId);
        if (user == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("사용자를 찾을 수 없습니다.");
        }
        user.setPassword(passwordEncoder.encode(password)); // 비밀번호 암호화하여 업데이트
        user.setName(name); // 이름 업데이트
        userCustomerMapper.updateUserCustomer(user); // DB에 업데이트
        return ResponseEntity.ok("사용자 정보가 성공적으로 업데이트 되었습니다.");
    }

    // 해당 이름으로 사용자를 찾고 이메일을 반환하는 메서드
    public String findEmailByName(String name) {
        UserCustomer user = userCustomerMapper.selectUserByName(name);
        if (user != null) {
            return user.getEmail();
        } else {
            throw new UsernameNotFoundException("해당 이름으로 등록된 사용자가 없습니다.");
        }
    }

    // 사용자의 이메일을 받아서 비밀번호 재설정 이메일을 보내는 메서드
    public void sendPasswordResetEmail(String email) {
        UserCustomer user = userCustomerMapper.selectUserByEmail(email);
        if (user != null) {
            // 대문자 알파벳 6자리 랜덤 비밀번호 생성
            String temporaryPassword = RandomStringUtils.randomAlphabetic(6).toUpperCase();
            // 비밀번호를 암호화하여 설정
            user.setPassword(passwordEncoder.encode(temporaryPassword));
            // 데이터베이스에 비밀번호 업데이트하고 결과를 확인
            int updatedRows = userCustomerMapper.updateUserCustomer(user);
            if (updatedRows == 0) {
                // 업데이트된 행이 없으면 예외를 발생시킵니다.
                throw new UpdateFailedException("유저 비밀번호 초기화에 실패하였습니다., no rows updated.");
            }
            
            // 비밀번호 재설정 이메일 전송
            try {
                emailService.sendPasswordResetEmail(email, temporaryPassword);
            } catch (IOException e) {
                log.error("비밀번호 재설정 이메일 전송에 실패했습니다.", e);
                throw new EmailSendingException("비밀번호 재설정 이메일 전송에 실패했습니다.");
            }
        } else {
            throw new UsernameNotFoundException("해당 이메일을 가진 사용자를 찾을 수 없습니다: " + email);
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

    // 개인정보 수정 기능
    public ResponseEntity<String> updateUserInfo(UserCustomer userCustomer) {
        try {
            userCustomer.setPassword(passwordEncoder.encode(userCustomer.getPassword())); // 비밀번호 암호화
            userCustomerMapper.updateUserCustomer(userCustomer); // 사용자 정보 업데이트
            return ResponseEntity.ok("사용자 정보가 업데이트 되었습니다.");
        } catch (DataAccessException e) {
            log.error("데이터베이스 오류", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("데이터베이스 오류가 발생했습니다.");
        } catch (Exception e) {
            log.error("알 수 없는 오류", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("알 수 없는 오류가 발생했습니다.");
        }
    }

    public boolean checkEmailExists(String email) {
         // 데이터베이스에서 email을 검색하여 존재하는지 확인하는 로직 구현
        UserCustomer user = userCustomerMapper.selectUserByEmail(email);
        return user != null;
    }

    public boolean sendVerificationEmail(String email) {
        String verificationCode = VerificationCodeGenerator.generate();
        try {
            emailService.sendVerificationEmail(email, verificationCode);
            // 데이터베이스에 인증 코드 저장 로직 추가 필요
            // 예: userCustomerMapper.updateVerificationCode(email, verificationCode);
            return true;
        } catch (IOException e) {
            log.error("인증 이메일 보내기에 실패하였습니다.", e);
            return false;
        }
    }

    public boolean verifyAccount(String code) {
        // 전달받은 코드로 사용자를 찾아서 상태를 업데이트하는 로직 구현
        UserCustomer user = userCustomerMapper.findByVerificationCode(code);
        if (user != null) {
            user.setState(1); // 인증된 사용자로 상태 변경, 상태 코드에 따라 달라질 수 있음
            user.setVerificationCode(null); // 인증 후 코드 제거
            userCustomerMapper.updateUserCustomer(user);
            return true;
        } else {
            return false;
        }
    }

    public boolean requestVerificationStore(String email) throws IOException {
        String verificationCode = VerificationCodeGenerator.generate();
        redisService.setValues(REDIS_KEY_EMAIL_PREFIX+email, verificationCode, Duration.ofMinutes(300));
        if(redisService.getValues(REDIS_KEY_EMAIL_PREFIX+email).equals(verificationCode)){
            emailService.sendVerificationEmail(email, verificationCode);
            return true;
        }
        return false;
    }

    public boolean verifyStore(String email, String code) {
        if(redisService.getValues(REDIS_KEY_EMAIL_PREFIX+email).equals(code)){
            redisService.deleteValues(REDIS_KEY_EMAIL_PREFIX+email);
            return true;
        }
        return false;
    }

    public int findStoreEmailCnt(String email) {
        System.out.println(userStoreMapper.findStoreEmailCnt(email));
        return userStoreMapper.findStoreEmailCnt(email);
    }
}