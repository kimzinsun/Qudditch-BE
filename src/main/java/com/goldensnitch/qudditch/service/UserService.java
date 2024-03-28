package com.goldensnitch.qudditch.service;

import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.goldensnitch.qudditch.dto.UserAdmin;
import com.goldensnitch.qudditch.dto.UserCustomer;
import com.goldensnitch.qudditch.dto.UserStore;
import com.goldensnitch.qudditch.repository.UserAdminRepository;
import com.goldensnitch.qudditch.repository.UserCustomerRepository;
import com.goldensnitch.qudditch.repository.UserStoreRepository;
@Service
public class UserService {

    private final UserCustomerRepository userCustomerRepository;
    private final UserStoreRepository userStoreRepository;
    private final UserAdminRepository userAdminRepository;
    private final PasswordEncoder passwordEncoder;
    private final EmailService emailService;

    private static final Logger log = LoggerFactory.getLogger(UserService.class);

    @Autowired
    public UserService(UserCustomerRepository userCustomerRepository,
                    UserStoreRepository userStoreRepository,
                    UserAdminRepository userAdminRepository,
                    PasswordEncoder passwordEncoder,
                    EmailService emailService) {
                    this.userCustomerRepository = userCustomerRepository;
                    this.userStoreRepository = userStoreRepository;
                    this.userAdminRepository = userAdminRepository;
                    this.passwordEncoder = passwordEncoder;
                    this.emailService = emailService;
    }

     // 일반유저 회원가입 로직
    public ResponseEntity<String> registerUserCustomer(UserCustomer userCustomer) {
        try {
            // 입력된 이메일이 이미 사용중인지 검사합니다.
            if (userCustomerRepository.findByEmail(userCustomer.getEmail()) != null) {
                log.error("이미 존재하는 이메일입니다: {}", userCustomer.getEmail());
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("이미 존재하는 이메일입니다.");
            }

            // 비밀번호를 암호화하고 신규 사용자 속성을 설정합니다.
            userCustomer.setPassword(passwordEncoder.encode(userCustomer.getPassword()));
            userCustomer.setState(0); // 미인증 사용자 상태로 가정합니다.
            userCustomer.setVerificationCode(UUID.randomUUID().toString()); // 인증 코드를 생성합니다.

            // 사용자 정보를 데이터베이스에 저장합니다.
            userCustomerRepository.insertUserCustomer(userCustomer);
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
        // 필수 필드에 대한 유효성 검사
    if (userStore.getStoreId() == null) {
        log.error("Store ID는 필수 항목입니다.");
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Store ID는 필수 항목입니다.");
    }
    if (userStore.getEmail() == null || userStore.getEmail().isEmpty()) {
        log.error("이메일은 필수 항목입니다.");
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("이메일은 필수 항목입니다.");
    }
    if (userStore.getPassword() == null || userStore.getPassword().isEmpty()) {
        log.error("비밀번호는 필수 항목입니다.");
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("비밀번호는 필수 항목입니다.");
    }
    if (userStore.getName() == null || userStore.getName().isEmpty()) {
        log.error("이름은 필수 항목입니다.");
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("이름은 필수 항목입니다.");
    }
    if (userStore.getStoreId() == null) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Store ID는 필수 항목입니다.");
    }
    if (userStore.getBnNumber() == null) {
        log.error("사업자 번호는 필수 항목입니다.");
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("사업자 번호는 필수 항목입니다.");
    }
    // 이메일 중복 검사
    if (userStoreRepository.findByEmail(userStore.getEmail()) != null) {
        log.error("이미 존재하는 이메일입니다: {}", userStore.getEmail());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("이미 존재하는 이메일입니다.");
    }
    
    // 계정 정보 설정 및 저장
    try {
        String encodedPassword = passwordEncoder.encode(userStore.getPassword());
        userStore.setPassword(encodedPassword);
        // 상태(state) 기본값 설정 (미인증 상태로 가정)
        userStore.setState(0); // 미인증 상태
        userStoreRepository.insertUserStore(userStore);
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
            if (userAdminRepository.findByEmail(userAdmin.getEmail()) != null) {
                log.error("이미 존재하는 이메일입니다: {}", userAdmin.getEmail());
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("이미 존재하는 이메일입니다.");
            }
        try {
            String encodedPassword = passwordEncoder.encode(userAdmin.getPassword());
            userAdmin.setPassword(encodedPassword);
            // 여기에 관리자 특화 등록 로직을 추가할 수 있습니다.
            userAdminRepository.insertUserAdmin(userAdmin);
            log.info("관리자 등록에 성공했습니다: {}", userAdmin.getEmail());
            return ResponseEntity.ok("관리자 등록에 성공했습니다.");
        } catch (Exception e) {
            log.error("관리자 등록에 실패했습니다: {}", userAdmin.getEmail(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("관리자 등록에 실패했습니다");
        }
    }
    
    /**
     * @return
     */
    public ResponseEntity<String> registerUserCustomerTest() {
        UserCustomer userCustomer = new UserCustomer();
        userCustomer.setEmail("kdt@dawoony.com");
        userCustomer.setPassword("testtest1!");
        userCustomer.setName("Test User");
        userCustomer.setState(0); // Assuming 1 is for active users

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
            emailService.sendVerificationEmail(userCustomer.getEmail(), verificationCode);

            log.info("User registered successfully: {}", userCustomer.getEmail());
        return ResponseEntity.ok("User registered successfully. Please check your email to verify your account.");
    } catch (Exception e) {
        log.error("Failed to register user: {}", userCustomer.getEmail(), e);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to register user");
    }

    // private void sendVerificationEmail(UserCustomer userCustomer) {
    //     String verificationCode = userCustomer.getVerificationCode();
    //     String verificationUrl = "http://yourdomain.com/verify?code=" + verificationCode;
        
    //     try {
    //         emailService.sendVerificationEmail(userCustomer.getEmail(), verificationUrl);
    //     } catch (IOException e) {
    //         log.error("Failed to send verification email to: {}", userCustomer.getEmail(), e);
    //         throw new EmailSendingException("Failed to send verification email");
    //     }
    // }
}
}