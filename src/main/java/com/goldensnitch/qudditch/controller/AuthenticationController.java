package com.goldensnitch.qudditch.controller;


import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.goldensnitch.qudditch.dto.AuthResponse;
import com.goldensnitch.qudditch.dto.LoginRequest;
import com.goldensnitch.qudditch.dto.RegisterStoreRequest;
import com.goldensnitch.qudditch.dto.SocialLoginDto;
import com.goldensnitch.qudditch.dto.UserAdmin;
import com.goldensnitch.qudditch.dto.UserCustomer;
import com.goldensnitch.qudditch.dto.UserStore;
import com.goldensnitch.qudditch.jwt.JwtTokenProvider;
import com.goldensnitch.qudditch.mapper.UserAdminMapper;
import com.goldensnitch.qudditch.mapper.UserCustomerMapper;
import com.goldensnitch.qudditch.service.ExtendedUserDetails;
import com.goldensnitch.qudditch.service.FileStorageService;
import com.goldensnitch.qudditch.service.OCRService;
import com.goldensnitch.qudditch.service.UserService;

@RestController
public class AuthenticationController {
    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider jwtTokenProvider; // JWT 토큰 제공자 의존성 주입
    private final UserCustomerMapper userCustomerMapper;
    private final UserService userService;
    private final PasswordEncoder passwordEncoder;
    private final UserAdminMapper userAdminMapper; // 생성자 주입 추가
    private static final Logger log = LoggerFactory.getLogger(AuthenticationController.class);

    @Autowired
    public AuthenticationController(
        AuthenticationManager authenticationManager,
        JwtTokenProvider jwtTokenProvider,
        UserCustomerMapper userCustomerMapper,
        UserService userService,
        PasswordEncoder passwordEncoder,
        UserAdminMapper userAdminMapper // 생성자 주입 추가
    ) {
        this.authenticationManager = authenticationManager;
        this.jwtTokenProvider = jwtTokenProvider;
        this.userCustomerMapper = userCustomerMapper;
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
        this.userAdminMapper = userAdminMapper; // 초기화 추가
    }


    @PostMapping("/login")
    public ResponseEntity<?> authenticateUser(@RequestBody LoginRequest loginRequest) {
        log.info("Attempting to authenticate user with email: " + loginRequest.getEmail());

        // 회원 여부 확인 로직
        UserCustomer user = userCustomerMapper.findByEmail(loginRequest.getEmail());
        if (user == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("회원가입이 필요합니다.");
        }

        // 비밀번호 검증 로직 (입력된 비밀번호와 데이터베이스에 저장된 해시를 비교)
        if (!passwordEncoder.matches(loginRequest.getPassword(), user.getPassword())) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("비밀번호가 틀렸습니다.");
        }

        // 인증 로직 (비밀번호 검증이 성공하면 토큰을 생성)
        Authentication authentication =
            new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword());

        authentication = authenticationManager.authenticate(authentication);
        SecurityContextHolder.getContext().setAuthentication(authentication);

        // JWT 토큰 생성
        String token = jwtTokenProvider.generateToken(authentication);
        AuthResponse authResponse = new AuthResponse(token);

        // 인증 후 응답 로그 변경
        log.info("Security Conte에 Authentication: {}", authResponse.getToken());
        // 생성된 토큰을 클라이언트에 반환
        log.info("User authenticated suessfully: " + authResponse);
        return ResponseEntity.ok(authResponse);
    }

    @PostMapping("/store/login")
    public ResponseEntity<?> authenticateStore(@RequestBody LoginRequest loginRequest) {
        log.info("Attempting to authenticate store with email: " + loginRequest.getEmail());
        Authentication authentication = authenticationManager
            .authenticate(new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword()));
        SecurityContextHolder.getContext().setAuthentication(authentication);
        // 토큰 생성 및 반환
        String token = jwtTokenProvider.generateToken(authentication);
        return ResponseEntity.ok(new AuthResponse(token));
    }

    // 소셜 로그인 및 이메일 인증 통합을 위한 컨트롤러 메소드 추가
    // @PostMapping("/social-login/naver")
    // public ResponseEntity<?> socialLogin(@RequestBody SocialLoginDto socialLoginDto) {
    //     // 네이버 소셜 로그인 후 받은 정보를 처리하는 로직을 구현
    //     // 이 때, 필요한 정보를 DTO로부터 받아오고 처리 결과를 반환합니다.
    //     // 예시로 AuthResponse를 사용하여 토큰과 함께 응답
    //     String token = "가상의 토큰"; // 이 부분은 실제 로직에 따라 생성된 토큰으로 대체해야 합니다.
    //     return ResponseEntity.ok(new AuthResponse(token));
    // }
        @PostMapping("/social-login/{provider}")
        public ResponseEntity<?> socialLogin(@PathVariable String provider, @RequestBody SocialLoginDto socialLoginDto) {
            // UserService의 계정 통합 로직 호출
            ExtendedUserDetails  user = (ExtendedUserDetails) userService.processUserIntegration(provider, socialLoginDto);
        
            if (user != null) {
                // 계정 통합 또는 생성 후 성공적으로 처리된 경우, JWT 토큰 발급 및 반환
                String token = jwtTokenProvider.generateToken(new UsernamePasswordAuthenticationToken(user.getEmail(), null, user.getAuthorities()));
                return ResponseEntity.ok(new AuthResponse(token));
            } else {
                // 처리 중 오류 발생 시
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("계정 처리 중 오류 발생");
            }
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
        UserCustomer user = userCustomerMapper.findByVerificationCode(code);

        if (user == null) {
            return ResponseEntity.badRequest().body("Invalid verification code.");
        }

        // 사용자 상태를 '일반'(1)으로 업데이트
        user.setState(1);
        user.setVerificationCode(null); // 인증 코드 사용 후 초기화
        userCustomerMapper.updateUserCustomer(user);

        return ResponseEntity.ok("Account verified successfully.");
    }

    @GetMapping("/self")
    public ResponseEntity<?> getSelf(Authentication authentication) {
        if (authentication != null && authentication.isAuthenticated()) {
            Object principal = authentication.getPrincipal();
            if (principal instanceof ExtendedUserDetails userDetails) {
                Map<String, Object> userInfo = new HashMap<>();
                userInfo.put("id", userDetails.getId());
                userInfo.put("name", userDetails.getName());
                userInfo.put("email", userDetails.getEmail());
                // 기타 상세 정보 추가...
                return ResponseEntity.ok(userInfo);
            } else {
                // 여기서 principal의 실제 클래스 타입을 로깅하여 더 많은 정보를 얻을 수 있습니다.
                log.error("Expected principal to be an instance of ExtendedUserDetails but found: {}", principal.getClass().getName());
                // 'principal'이 'ExtendedUserDetails'의 인스턴스가 아닌 경우 처리
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User details not found");
            }
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Unauthorized");
        }
    }

    // 일반 유저 회원가입을 위한 엔드포인트
    @PostMapping("/register/customer")
    public ResponseEntity<?> registerCustomer(@RequestBody UserCustomer userCustomer) {
        log.info("Registering customer with email: {}", userCustomer.getEmail());
        // UserService의 회원가입 로직을 호출하여 처리결과를 반환한다.
        return userService.registerUserCustomer(userCustomer);
    }

    @Autowired
    private OCRService ocrService;

    @Autowired
    private FileStorageService fileStorageService;

    // 점주 유저 회원가입을 위한 엔드포인트
    @PostMapping("/register/store")
    public ResponseEntity<?> registerStore(@ModelAttribute RegisterStoreRequest request) {
        try {
            String fileUrl = fileStorageService.storeFile(request.getBusinessLicenseFile());
            String extractedBusinessNumber = ocrService.extractBusinessNumber(request.getBusinessLicenseFile());

            if (!request.getBusinessNumber().equals(extractedBusinessNumber)) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("사업자 등록증 번호가 일치하지 않습니다.");
            }

            // 여기에서 나머지 회원가입 로직을 추가...
            // 예: UserStore 객체 생성 및 userService.registerUserStore 호출
            // 반환된 결과를 ResponseEntity로 감싸서 반환

            return ResponseEntity.ok("점주 등록이 완료되었습니다. 파일 저장 경로: " + fileUrl);
        } catch (Exception e) {
            // 에러 로깅
            Logger log = LoggerFactory.getLogger(AuthenticationController.class);
            log.error("회원가입 처리 중 오류 발생", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("회원가입 처리 중 오류가 발생했습니다.");
        }
    }

    @GetMapping("/store/search")
    public ResponseEntity<List<UserStore>> searchStores(@RequestParam String name) {
    List<UserStore> stores = userService.searchStoresByName(name);
    return ResponseEntity.ok(stores);
}


    @PostMapping("/admin/login")
public ResponseEntity<?> authenticateAdmin(@RequestBody LoginRequest loginRequest) {
    log.info("Attempting to authenticate admin with email: {}", loginRequest.getEmail());

    // 관리자 여부 확인 로직
    UserAdmin admin = userAdminMapper.findByEmail(loginRequest.getEmail());
    if (admin == null) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("관리자 계정이 존재하지 않습니다.");
    }

    // 비밀번호 검증 로직
    if (!passwordEncoder.matches(loginRequest.getPassword(), admin.getPassword())) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("비밀번호가 틀렸습니다.");
    }

    // 인증 로직
    Authentication authentication =
        new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword());
    authentication = authenticationManager.authenticate(authentication);
    SecurityContextHolder.getContext().setAuthentication(authentication);

    // JWT 토큰 생성
    String token = jwtTokenProvider.generateToken(authentication);
    AuthResponse authResponse = new AuthResponse(token);

    log.info("Admin authenticated successfully: {}", authResponse);
    return ResponseEntity.ok(authResponse);
}
    
}
