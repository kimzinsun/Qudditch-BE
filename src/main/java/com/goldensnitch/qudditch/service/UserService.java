package com.goldensnitch.qudditch.service;

import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.goldensnitch.qudditch.dto.UserCustomer;
import com.goldensnitch.qudditch.repository.UserCustomerRepository;
@Service
public class UserService {

    private final UserCustomerRepository userCustomerRepository;
    private final PasswordEncoder passwordEncoder;
  //  private final EmailService emailService;

    private static final Logger log = LoggerFactory.getLogger(UserService.class);

    
    public UserService(UserCustomerRepository userCustomerRepository,
                    PasswordEncoder passwordEncoder
                   // EmailService emailService
                    ) {
        this.userCustomerRepository = userCustomerRepository;
        this.passwordEncoder = passwordEncoder;
     //   this.emailService = emailService;
    }

    public void registerUserCustomer(UserCustomer userCustomer) {
        try {
            // 비밀번호 암호화
            // String encodedPassword = passwordEncoder.encode(userCustomer.getPassword());
            // userCustomer.setPassword(encodedPassword);

            // 인증 코드 생성 및 설정
            String verificationCode = UUID.randomUUID().toString();
            userCustomer.setVerificationCode(verificationCode);

            // 사용자 상태를 '미인증'(0)으로 설정
            userCustomer.setState(0);

            // 사용자 정보 저장
            userCustomerRepository.insertUserCustomer(userCustomer);

            // 인증 이메일 전송
        //    sendVerificationEmail(userCustomer);

            log.info("User registered successfully: {}", userCustomer.getEmail());
        } catch (Exception e) {
            log.error("Failed to register user: {}", userCustomer.getEmail(), e);
            throw new RuntimeException("Failed to register user", e);
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
