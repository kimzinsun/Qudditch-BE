package com.goldensnitch.qudditch.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class NaverMailService {

    private final RestTemplate restTemplate;

    @Value("${naver.api.client-id}")
    private String clientId;

    @Value("${naver.api.client-secret}")
    private String clientSecret;

    @Value("${naver.api.mail-url}")
    private String mailUrl;

    public NaverMailService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public void sendVerificationEmail(String toEmail, String verificationCode) {
        String apiUrl = String.format("%s/users/me/mail", mailUrl);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("X-Naver-Client-Id", clientId);
        headers.set("X-Naver-Client-Secret", clientSecret);

        String mailBody = String.format(
            "{\"to\": \"%s\", \"subject\": \"Email Verification\", \"body\": \"Your verification code is: %s\"}", 
            toEmail, 
            verificationCode
        );

        HttpEntity<String> request = new HttpEntity<>(mailBody, headers);

        restTemplate.postForObject(apiUrl, request, String.class);
    }
}