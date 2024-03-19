package com.goldensnitch.qudditch.config;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.client.RestTemplate;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final UserDetailsService userDetailsService;
    private final ClientRegistrationRepository clientRegistrationRepository;

    public SecurityConfig(UserDetailsService userDetailsService, ClientRegistrationRepository clientRegistrationRepository) {
        this.userDetailsService = userDetailsService;
        this.clientRegistrationRepository = clientRegistrationRepository;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder());
        return auth.build();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .authorizeHttpRequests(authorize -> authorize
                .requestMatchers("/home", "/login").authenticated()
                .anyRequest().permitAll())
            .formLogin(formLogin -> formLogin
                .loginPage("/login")
                .defaultSuccessUrl("/loginSuccess", true)
                .failureUrl("/loginFailure")
                .permitAll())
            .logout(logout -> logout
                .permitAll())
            .oauth2Login(oauth2Login -> oauth2Login
                .loginPage("/login")
                .defaultSuccessUrl("/loginSuccess")
                .failureUrl("/loginFailure")
                .clientRegistrationRepository(clientRegistrationRepository)); // OAuth2 클라이언트 설정 추가
        return http.build();
    }



    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }
    


}
