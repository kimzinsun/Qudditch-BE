package com.goldensnitch.qudditch.config;

import com.goldensnitch.qudditch.jwt.JwtTokenFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
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
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
    private final JwtTokenFilter jwtTokenFilter;

    @Autowired
    public SecurityConfig(JwtTokenFilter jwtTokenFilter) {
        this.jwtTokenFilter = jwtTokenFilter;
    }

    // PasswordEncoder 빈 정의
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

<<<<<<< HEAD
    // 스프링 시큐리티가 제공하는 기본 구성을 사용하여
    // AuthenticationManager를 설정합니다.
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService)
                .passwordEncoder(passwordEncoder());
        // 기존 록지 + jwttokenfilter추가

    }

=======
>>>>>>> f5e8cedd165d97ff2afbcfcb5524655cbb23f847
    // AuthenticationManager 빈을 생성합니다.
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    protected CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();

        configuration.setAllowCredentials(true);
//        configuration.setAllowedOrigins(List.of("http://localhost:3000", "https://hdw-mbp.taile3da1.ts.net"));
        configuration.setAllowedOriginPatterns(List.of("*"));
        configuration.setAllowedMethods(List.of("HEAD", "GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS"));
        configuration.setAllowedHeaders(List.of("*"));
        configuration.setExposedHeaders(List.of("*"));

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

    // SecurityFilterChain 빈 정의
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http//  CSRF 비활성화
<<<<<<< HEAD
                .csrf(AbstractHttpConfigurer::disable)
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                //  세션 관리 정책 설정
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> auth
                        // 권한 설정 및 접근
//                .requestMatchers("/self").authenticated()
//                .requestMatchers("/public/**", "/login", "/store/login","/admin/login", "/test/register", "/register/customer", "/register/store", "/register/admin").permitAll()
//                .requestMatchers("/user/**").hasRole("USER")    // 일반 유저만 접근 가능
//                .requestMatchers("/store/**").hasRole("STORE")  // 점주만 접근 가능
//                .requestMatchers("/admin/**").hasRole("ADMIN")  // 관리자만 접근 가능
                        .anyRequest().permitAll())  // 나머지 경로는 인증된 사용자만 접근 가능
                // 4. 사용자 권한에 따른 UI 구성
                // 일반 유저: 기본적인 서비스 화면.
                // 점주: 매출 그래프, 매장 관리 탭 추가. (데이터베이스 또는 서비스 레이어에서 권한에 따른 데이터 접근 로직을 구현 EX.점주는 자신의 매장에 대한정보만 조회할 수 있어야한다.)
                // 관리자: 발주 관리, 시스템 관리 탭 추가.
//                .oauth2Login(Customizer.withDefaults())
//                .formLogin(AbstractHttpConfigurer::disable)
//                .logout(Customizer.withDefaults());

//             .oauth2Login(oauth -> oauth
// //                .loginPage("/login")
//                 .defaultSuccessUrl("/loginSuccess")
//                 .failureUrl("/loginFailure")
//                 .clientRegistrationRepository(clientRegistrationRepository))
                .oauth2Login(AbstractHttpConfigurer::disable) // OAuth2 로그인 비활성화
                .formLogin(AbstractHttpConfigurer::disable)  // 폼 로그인 비활성화
//        .formLogin(form -> form
//            .loginPage("/login")
//            .defaultSuccessUrl("/loginSuccess", true)  // 로그인 성공 시 리다이렉트될 URL
//            .failureUrl("/loginFailure")  // 로그인 실패 시 리다이렉트될 URL
//            .permitAll())
                .logout(logout -> logout
                        .logoutSuccessUrl("/login")
                        .deleteCookies("JSESSIONID")
                        .permitAll())
                // JwtTokenFilter를 필터 체인에 추가합니다.
                .addFilterBefore(jwtTokenFilter, UsernamePasswordAuthenticationFilter.class);
=======
            .csrf(AbstractHttpConfigurer::disable)
            .cors(cors -> cors.configurationSource(corsConfigurationSource()))
            //  세션 관리 정책 설정
            .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .authorizeHttpRequests(auth -> auth
                .anyRequest().permitAll())
            .oauth2Login(oauth2 -> oauth2
                .redirectionEndpoint(redirection -> redirection
                    .baseUri("/oauth2/callback/*"))
                .userInfoEndpoint(userInfo -> userInfo
                    .userService(oauth2UserService())) // 이 메서드는 oauth2UserService를 참조합니다
            )
            .logout(logout -> logout
                .logoutSuccessUrl("/login")
                .deleteCookies("JSESSIONID")
                .permitAll())
            // JwtTokenFilter를 필터 체인에 추가합니다.
            .addFilterBefore(jwtTokenFilter, UsernamePasswordAuthenticationFilter.class);
>>>>>>> f5e8cedd165d97ff2afbcfcb5524655cbb23f847

        return http.build();
    }

    @Bean
    public OAuth2UserService<OAuth2UserRequest, OAuth2User> oauth2UserService() {
        // DefaultOAuth2UserService를 통해 user 정보를 가져옵니다.
        // 필요한 경우 OAuth2User에 대한 추가 처리를 수행하고 반환합니다.
        // 예를 들어, 가져온 사용자 정보를 기반으로 데이터베이스에서 사용자를 찾거나 생성할 수 있습니다.
        return new DefaultOAuth2UserService();
    }

    // RestTemplate 빈을 생성합니다.
    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }


}