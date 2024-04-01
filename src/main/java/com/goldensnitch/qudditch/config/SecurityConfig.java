// package com.goldensnitch.qudditch.config;

// import org.springframework.context.annotation.Bean;
// import org.springframework.context.annotation.Configuration;
// import org.springframework.security.authentication.AuthenticationManager;
// import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
// import org.springframework.security.config.annotation.web.builders.HttpSecurity;
// import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
// import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
// import org.springframework.security.core.userdetails.UserDetailsService;
// import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
// import org.springframework.security.crypto.password.PasswordEncoder;
// import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
// import org.springframework.security.web.SecurityFilterChain;
// import org.springframework.web.client.RestTemplate;

// @Configuration
// @EnableWebSecurity
// public class SecurityConfig {

//     private final UserDetailsService userDetailsService;
//     private final ClientRegistrationRepository clientRegistrationRepository;

//     public SecurityConfig(UserDetailsService userDetailsService, ClientRegistrationRepository clientRegistrationRepository) {
//         this.userDetailsService = userDetailsService;
//         this.clientRegistrationRepository = clientRegistrationRepository;
//     }

//     @Bean
//     public PasswordEncoder passwordEncoder() {
//         return new BCryptPasswordEncoder();
//     }

//     // AuthenticationManagerBuilder를 사용하여 AuthenticationManager 빈을 생성합니다.
//     @Bean
//     public AuthenticationManager authenticationManager(AuthenticationManagerBuilder auth) throws Exception {
//         auth.userDetailsService(userDetailsService)
//             .passwordEncoder(passwordEncoder());
//         return auth.build();
//     }

//     @Bean
//     public WebSecurityCustomizer webSecurityCustomizer() {
//         return (web) -> web.ignoring().requestMatchers("/resources/**", "/static/**", "/css/**", "/js/**", "/images/**");
//     }

//     @Bean
//     public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
//         http
//             .authorizeHttpRequests(authorize -> authorize
//                 .requestMatchers("/home", "/login").authenticated()
//                 .anyRequest().permitAll())
//             .formLogin(formLogin -> formLogin
//                 .loginPage("/login")
//                 .defaultSuccessUrl("/loginSuccess", true)
//                 .failureUrl("/loginFailure")
//                 .permitAll())
//             .logout(logout -> logout
//                 .permitAll())
//             .oauth2Login(oauth2Login -> oauth2Login
//                 .loginPage("/login")
//                 .defaultSuccessUrl("/loginSuccess")
//                 .failureUrl("/loginFailure")
//                 .clientRegistrationRepository(clientRegistrationRepository)); // OAuth2 클라이언트 설정 추가
//         return http.build();
//     }


//     @Bean
//     public RestTemplate restTemplate() {
//         return new RestTemplate();
//     }


// }
package com.goldensnitch.qudditch.config;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.client.RestTemplate;

import com.goldensnitch.qudditch.jwt.JwtTokenFilter;
import com.goldensnitch.qudditch.service.CustomUserDetailsService;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
    private final CustomUserDetailsService userDetailsService;
    private final JwtTokenFilter jwtTokenFilter;

    @Autowired
    public SecurityConfig(CustomUserDetailsService userDetailsService, JwtTokenFilter jwtTokenFilter) {
        this.userDetailsService = userDetailsService;
        this.jwtTokenFilter = jwtTokenFilter;
    }

    // PasswordEncoder 빈 정의
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    // 스프링 시큐리티가 제공하는 기본 구성을 사용하여
    // AuthenticationManager를 설정합니다.
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService)
            .passwordEncoder(passwordEncoder());
        // 기존 록지 + jwttokenfilter추가

    }

    // AuthenticationManager 빈을 생성합니다.
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    // SecurityFilterChain 빈 정의
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http//  CSRF 비활성화
            .csrf(AbstractHttpConfigurer::disable)
            //  세션 관리 정책 설정
            .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .authorizeHttpRequests(auth -> auth
                // 권한 설정 및 접근
                
            // 4. 사용자 권한에 따른 UI 구성
            // 일반 유저: 기본적인 서비스 화면.
            // 점주: 매출 그래프, 매장 관리 탭 추가. (데이터베이스 또는 서비스 레이어에서 권한에 따른 데이터 접근 로직을 구현 EX.점주는 자신의 매장에 대한정보만 조회할 수 있어야한다.)
            // 관리자: 발주 관리, 시스템 관리 탭 추가.
                .requestMatchers("/self").authenticated()
                .requestMatchers("/public/**", "/login", "/store/login","/admin/login", "/test/register", "/register/customer", "/register/store", "/register/admin").permitAll()
                .requestMatchers("/user/**").hasRole("USER")    // 일반 유저만 접근 가능
                .requestMatchers("/store/**").hasRole("STORE")  // 점주만 접근 가능
                .requestMatchers("/admin/**").hasRole("ADMIN")  // 관리자만 접근 가능
                .anyRequest().authenticated())  // 나머지 경로는 인증된 사용자만 접근 가능
                // SecurityConfig.java에서 oauth2Login 설정 부분
                .oauth2Login(oauth2 -> oauth2
                    .redirectionEndpoint(redirection -> redirection
                    .   baseUri("/oauth2/callback/*"))
                    .userInfoEndpoint(userInfo -> userInfo
                        .userService(oauth2UserService())) // 이 메서드는 oauth2UserService를 참조합니다
            )
            .logout(logout -> logout
                .logoutSuccessUrl("/login")
                .deleteCookies("JSESSIONID")
                .permitAll())
            // JwtTokenFilter를 필터 체인에 추가합니다.
            .addFilterBefore(jwtTokenFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public OAuth2UserService<OAuth2UserRequest, OAuth2User> oauth2UserService() {
        DefaultOAuth2UserService defaultService = new DefaultOAuth2UserService();
        return userRequest -> {
            // DefaultOAuth2UserService를 통해 user 정보를 가져옵니다.
            OAuth2User oAuth2User = defaultService.loadUser(userRequest);
            
            // 필요한 경우 OAuth2User에 대한 추가 처리를 수행하고 반환합니다.
            // 예를 들어, 가져온 사용자 정보를 기반으로 데이터베이스에서 사용자를 찾거나 생성할 수 있습니다.

            return oAuth2User;
        };
    }

    // RestTemplate 빈을 생성합니다.
    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }


}
