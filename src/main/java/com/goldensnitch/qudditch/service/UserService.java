package com.goldensnitch.qudditch.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
  //  private final EmailService emailService;

    private static final Logger log = LoggerFactory.getLogger(UserService.class);

    
    public UserService(UserCustomerRepository userCustomerRepository,
                    UserStoreRepository userStoreRepository,
                    UserAdminRepository userAdminRepository,
                    PasswordEncoder passwordEncoder
                   // EmailService emailService
                    ) {
                    this.userCustomerRepository = userCustomerRepository;
                    this.userStoreRepository = userStoreRepository;
                    this.userAdminRepository = userAdminRepository;
                    this.passwordEncoder = passwordEncoder;
     //   this.emailService = emailService;
    }

    // 일반유저 회원가입 로직
    public ResponseEntity<String> registerUserCustomer(UserCustomer userCustomer) {
        try {
            String encodedPassword = passwordEncoder.encode(userCustomer.getPassword());
            userCustomer.setPassword(encodedPassword);
            // 아래 코드는 이메일 인증 구현 시 활성화됩니다.
            // userCustomer.setVerificationCode(UUID.randomUUID().toString());
            userCustomer.setState(0); // 0은 미인증 상태를 의미하며, 인증 후 상태를 업데이트해야 합니다.
            userCustomerRepository.insertUserCustomer(userCustomer);
            log.info("고객 등록에 성공했습니다: {}", userCustomer.getEmail());
            return ResponseEntity.ok("고객 등록에 성공했습니다.");
        } catch (Exception e) {
            log.error("고객 등록에 실패했습니다: {}", userCustomer.getEmail(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("고객 등록에 실패했습니다");
        }
    }

    // 점주 회원가입 로직

    public ResponseEntity<String> registerUserStore(UserStore userStore) {
        try {
            String encodedPassword = passwordEncoder.encode(userStore.getPassword());
            userStore.setPassword(encodedPassword);
            // 여기에 점주 특화 등록 로직을 추가할 수 있습니다.
            userStoreRepository.insertUserStore(userStore);
            log.info("점포 등록에 성공했습니다: {}", userStore.getEmail());
            return ResponseEntity.ok("점포 등록에 성공했습니다.");
        } catch (Exception e) {
            log.error("점포 등록에 실패했습니다: {}", userStore.getEmail(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("점포 등록에 실패했습니다");
        }
    }
    // 관리자 회원가입 로직

    public ResponseEntity<String> registerUserAdmin(UserAdmin userAdmin) {
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
        userCustomer.setState(1); // Assuming 1 is for active users

        try {
            // 비밀번호 암호화
            String encodedPassword = passwordEncoder.encode(userCustomer.getPassword());
            userCustomer.setPassword(encodedPassword);

            // 인증 코드 생성 및 설정
//            String verificationCode = UUID.randomUUID().toString();
//            userCustomer.setVerificationCode(verificationCode);

            // 사용자 상태를 '미인증'(0)으로 설정
            //userCustomer.setState(1);

            // 사용자 정보 저장
            userCustomerRepository.insertUserCustomer(userCustomer);

            // 인증 이메일 전송
//            sendVerificationEmail(userCustomer);

//             log.info("User registered successfully: {}", userCustomer.getEmail());
//         } catch (Exception e) {
//             log.error("Failed to register user: {}", userCustomer.getEmail(), e);
//             throw new RuntimeException("Failed to register user", e);
//         }
//         return null;
//     }
// }

        log.info("Test user registered successfully: {}", userCustomer.getEmail());
                    return ResponseEntity.ok("Test user registered successfully");
                } catch (Exception e) {
                    log.error("Failed to register test user: {}", userCustomer.getEmail(), e);
                    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to register test user");
                }
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
