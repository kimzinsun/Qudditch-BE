package com.goldensnitch.qudditch.controller;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.goldensnitch.qudditch.dto.AuthResponse;
import com.goldensnitch.qudditch.dto.LoginRequest;
import com.goldensnitch.qudditch.dto.SocialLoginDto;
import com.goldensnitch.qudditch.dto.UserCustomer;
import com.goldensnitch.qudditch.jwt.JwtTokenProvider;
import com.goldensnitch.qudditch.repository.UserCustomerRepository;

@RestController
public class AuthenticationController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtTokenProvider jwtTokenProvider; // JWT 토큰 제공자 의존성 주입

    @Autowired
    private UserCustomerRepository userCustomerRepository;

    // @Autowired
    // private UserService userService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    private static final Logger log = LoggerFactory.getLogger(AuthenticationController.class);

    

    @PostMapping("/login")
    public ResponseEntity<?> authenticateUser(@RequestBody LoginRequest loginRequest) {
        log.info("Attempting to authenticate user with email: " + loginRequest.getEmail());

        // 회원 여부 확인 로직
        UserCustomer user = userCustomerRepository.findByEmail(loginRequest.getEmail());
        if (user == null) {
            return ResponseEntity
                .status(HttpStatus.UNAUTHORIZED)
                .body("회원가입이 필요합니다.");
        }

        // 비밀번호 검증 로직 (입력된 비밀번호와 데이터베이스에 저장된 해시를 비교)
    if (!passwordEncoder.matches(loginRequest.getPassword(), user.getPassword())) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("비밀번호가 틀렸습니다.");
    }

        // 인증 로직 (비밀번호 검증이 성공하면 토큰을 생성)
    Authentication authentication = new UsernamePasswordAuthenticationToken(
        loginRequest.getEmail(),
        loginRequest.getPassword()
    );

    authentication = authenticationManager.authenticate(authentication);
    SecurityContextHolder.getContext().setAuthentication(authentication);

        // // 인증 로직
        // Authentication authentication = authenticationManager.authenticate(
        //     new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword()));

        
        // // SecurityContext에 인증 정보를 저장
        // SecurityContextHolder.getContext().setAuthentication(authentication);

        // JWT 토큰 생성
        String token = jwtTokenProvider.generateToken(authentication);
        AuthResponse authResponse = new AuthResponse(token);

        // 인증 후 응답 로그 변경
        log.info("Security Conte에 Authentication: {}", authResponse.getToken());
        // 생성된 토큰을 클라이언트에 반환
        log.info("User authenticated suessfully: " + authResponse);
        return ResponseEntity.ok(authResponse);
    }

    // @PostMapping("/test/register")
    // public ResponseEntity<String> registerUser() {
    //     UserCustomer userCustomer = new UserCustomer();
    //     userCustomer.setEmail("ttt@test.com");
    //     userCustomer.setPassword("1234");
    //     userCustomer.setName("test");
    //     return userService.registerUserCustomerTest(userCustomer);
    // }


    @PostMapping("/store/login")
    public ResponseEntity<?> authenticateStore(@RequestBody LoginRequest loginRequest) {
        log.info("Attempting to authenticate store with email: " + loginRequest.getEmail());
        Authentication authentication = authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword()));
        SecurityContextHolder.getContext().setAuthentication(authentication);
        // 토큰 생성 및 반환
        String token = jwtTokenProvider.generateToken(authentication);
        return ResponseEntity.ok(new AuthResponse(token));
    }

    // 소셜 로그인 및 이메일 인증 통합을 위한 컨트롤러 메소드 추가
    @PostMapping("/social-login/naver")
    public ResponseEntity<?> socialLogin(@RequestBody SocialLoginDto socialLoginDto) {
        // 네이버 소셜 로그인 후 받은 정보를 처리하는 로직을 구현
        // 이 때, 필요한 정보를 DTO로부터 받아오고 처리 결과를 반환합니다.
        // 예시로 AuthResponse를 사용하여 토큰과 함께 응답
        String token = "가상의 토큰"; // 이 부분은 실제 로직에 따라 생성된 토큰으로 대체해야 합니다.
        return ResponseEntity.ok(new AuthResponse(token));
    }

    @GetMapping("/loginSuccess")
    public String loginSuccess(@AuthenticationPrincipal OAuth2User user) {
        // 로그인 성공 후 사용자 정보 처리
        // 'user' 객체에는 네이버로부터 받은 사용자 정보가 들어 있습니다.
        return "로그인에 성공했습니다.";
    }

    @GetMapping("/loginFailure")
    public String loginFailure() {
        // 로그인 실패 처리
        return "로그인에 실패했습니다.";
    }

    @GetMapping("/verify")
    public ResponseEntity<String> verifyUser(@RequestParam("code") String code) {
        UserCustomer user = userCustomerRepository.findByVerificationCode(code);

        if (user == null) {
            return ResponseEntity.badRequest().body("Invalid verification code.");
        }

        // 사용자 상태를 '일반'(1)으로 업데이트
        user.setState(1);
        user.setVerificationCode(null); // 인증 코드 사용 후 초기화
        userCustomerRepository.updateUserCustomer(user);

        return ResponseEntity.ok("Account verified successfully.");
    }

    // @GetMapping("/self")
    // public void getSelf() {
    //     log.info("ContextHolder {}",SecurityContextHolder.getContext().getAuthentication().getPrincipal());
    // }
    //     @GetMapping("/self")
    // public ResponseEntity<?> getSelf(Authentication authentication) {
    //     if (authentication == null || !authentication.isAuthenticated()) {
    //         return new ResponseEntity<>("Unauthorized", HttpStatus.UNAUTHORIZED);
    //     }

    //     // 여기서는 사용자의 세부 정보를 반환합니다. 예를 들어:
    //     Object principal = authentication.getPrincipal();
    //     return ResponseEntity.ok(principal);
    // }
        @GetMapping("/self")
    public ResponseEntity<?> getSelf(Authentication authentication) {
        if (authentication == null || !authentication.isAuthenticated()) {
            return new ResponseEntity<>("Unauthorized", HttpStatus.UNAUTHORIZED);
        }

        // 사용자 정보 출력, 여기서는 예시로 authentication 객체를 직접 반환
        return ResponseEntity.ok(authentication);
    }

}
