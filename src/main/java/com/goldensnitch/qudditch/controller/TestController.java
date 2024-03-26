// package com.goldensnitch.qudditch.controller;

// import com.goldensnitch.qudditch.service.EmailService;
// import com.goldensnitch.qudditch.service.TestService;
// import lombok.extern.slf4j.Slf4j;

// import java.io.IOException;

// import org.springframework.web.bind.annotation.GetMapping;
// import org.springframework.web.bind.annotation.RequestMapping;
// import org.springframework.web.bind.annotation.RestController;

// @Slf4j
// @RestController
// @RequestMapping("/api/test")
// public class TestController {
//     private final TestService testService;
//     private final EmailService emailService;

    
//     public TestController(TestService testService, EmailService emailService) {
//         this.testService = testService;
//         this.emailService = emailService;
//     }

//     @GetMapping("")
// public String test() {
//     String test = testService.test();
//     log.info("TestController.test: {}", test);
//     String email = "kdt@dawoony.com"; // 이메일 주소
//     String verificationCode = "123456"; // 임의의 인증 코드, 실제 구현에서는 동적으로 생성될 것입니다.
//     try {
//         emailService.sendVerificationEmail(email, verificationCode);
//         return "Email sent successfully";
//     } catch (IOException e) {
//         log.error("Failed to send email", e);
//         return "Failed to send email";
//     }
// }
// }
