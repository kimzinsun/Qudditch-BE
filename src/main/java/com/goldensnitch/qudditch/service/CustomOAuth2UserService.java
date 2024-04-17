package com.goldensnitch.qudditch.service;

import java.util.HashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import com.goldensnitch.qudditch.dto.UserCustomer;
import com.goldensnitch.qudditch.mapper.SocialLoginMapper;
import com.goldensnitch.qudditch.mapper.UserCustomerMapper;

// CustomOAuth2UserService.java
// 카카오 로그인을 포함한 OAuth2 로그인 정보를 처리하는 서비스
@Service
public class CustomOAuth2UserService implements OAuth2UserService<OAuth2UserRequest, OAuth2User> {

    // UserCustomerMapper와 SocialLoginMapper에 대한 의존성 주입
    @Autowired
    private UserCustomerMapper userCustomerMapper;
    @Autowired
    private SocialLoginMapper socialLoginMapper;

     // 카카오 로그인에서 받은 정보를 처리하는 메서드입니다.
    // @Override
    // public OAuth2User loadUser(String string) throws OAuth2AuthenticationException {
    //     // OAuth2 로그인으로부터 사용자 정보를 가져오는 기본 서비스
    //     DefaultOAuth2UserService delegate = new DefaultOAuth2UserService();
    //     OAuth2User oAuth2User = delegate.loadUser(string);

    //     // OAuth2 프로바이더에서 받은 이메일이 이미 `user_customer` 테이블에 있는지 확인
    //     String email = oAuth2User.<String>getAttribute("account_email");

    //     // Check if the user is already present in the 'user_customer' table
    //     UserCustomer user = userCustomerMapper.findByEmail(email);
        
    //     // 새로운 소셜 로그인 정보를 `SocialLogin` 테이블에 추가
    //     String token = string.getAccessToken().getTokenValue();
    //     String regType = string.getClientRegistration().getRegistrationId(); // 'google', 'naver', 'kakao'
    //     SocialLogin socialLogin = new SocialLogin(token, email, oAuth2User.getAttribute("name"), regType);
    //     socialLoginMapper.insertSocialLogin(socialLogin);

    //     // 시스템에 사용자가 없는 경우 새로운 `user_customer` 정보를 등록
    //     if (user == null) {
    //         user = new UserCustomer();
    //         // 사용자 기본 정보 설정
    //         user.setEmail(email);
    //         // 여기에서 추가적인 사용자 정보를 설정할 수 있습니다.
    //         // 예: user.setName(name);
    //         userCustomerMapper.insertUserCustomer(user);
    //     }

    //     // CustomOAuth2User 클래스를 사용해 OAuth2User 정보를 반환할 수 있습니다.
    //     return new CustomOAuth2User(oAuth2User.getAttributes(), oAuth2User.getAuthorities(), regType);
    // }

    @Override
public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
    DefaultOAuth2UserService delegate = new DefaultOAuth2UserService();
    OAuth2User oAuth2User = delegate.loadUser(userRequest);

    // OAuth2 프로바이더에서 받은 사용자 정보
    String email = oAuth2User.getAttribute("account_email");
    String name = oAuth2User.getAttribute("name");

    // user_customer 테이블에서 사용자 조회
    UserCustomer user = userCustomerMapper.findByEmail(email);

     // 새로운 소셜 로그인 정보를 SocialLogin 테이블에 저장
    String token = userRequest.getAccessToken().getTokenValue();
    String regType = userRequest.getClientRegistration().getRegistrationId(); // 'kakao'
    SocialLogin socialLogin = new SocialLogin(token, email, name, regType);
    socialLoginMapper.insertSocialLogin(socialLogin);

    // user_customer 테이블에 사용자가 없으면 새로 등록
    if (user == null) {
        user = new UserCustomer();
        user.setEmail(email);
        user.setName(name);
        userCustomerMapper.insertUserCustomer(user);
        // 기타 필요한 필드 설정
    }

    // 이제 생성된 UserCustomer Eail를 SocialLogin과 연결할 수 있습니다.
    socialLogin.setEmail(user.getEmail());

    return new CustomOAuth2User(oAuth2User.getAttributes(), oAuth2User.getAuthorities(), "name");
}

    public  String getAccessToken(String code) {
        // code를 사용하여 카카오 API로부터 액세스 토큰을 받아오는 로직 구현
        // 이 부분에 HTTP 요청을 통해 카카오 API에 액세스 토큰을 요청하고
        // 응답에서 액세스 토큰을 추출하여 반환하는 코드를 작성합니다.
        String accessToken = "가상의_액세스_토큰";
        return accessToken;
    }

    public  HashMap<String, Object> getUserInfo(String accessToken) {
        // accessToken을 사용하여 카카오 API로부터 사용자 정보를 받아오는 로직 구현
        // 이 부분에 HTTP 요청을 통해 카카오 API에 사용자 정보를 요청하고
        // 응답을 HashMap<String, Object> 형태로 변환하여 반환하는 코드를 작성합니다.
        HashMap<String, Object> userInfo = new HashMap<>();
        userInfo.put("email", "");
        userInfo.put("name", "가상의_이름");
        return userInfo;
}
}