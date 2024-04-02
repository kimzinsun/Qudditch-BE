package com.goldensnitch.qudditch.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
// 사업자 번호 검증 api연동 클래스
public class BusinessNumberVerificationService {

    
    @Value("${naver.cloud.businessNumberVerificationUrl}")
    private String verificationUrl;

    @Value("${naver.cloud.apiKey}")
    private String apiKey;

    private final RestTemplate restTemplate;

    @Autowired
    public BusinessNumberVerificationService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public boolean verifyBusinessNumber(String businessNumber) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("X-NCP-APIGW-API-KEY-ID", apiKey);

        // 사업자 번호 데이터 포맷에 따라 조정이 필요할 수 있음
        HttpEntity<String> request = new HttpEntity<>(businessNumber, headers);
        
        ResponseEntity<String> response = restTemplate.postForEntity(verificationUrl, request, String.class);
        
        // API 응답에 따라 유효성 검증 로직 구현
        // 예시는 API 응답을 바탕으로 한 것이므로 실제 API 응답 구조에 맞게 수정 필요
        return response.getStatusCode() == HttpStatus.OK
            && response.getBody().contains("유효한 사업자 번호입니다."); // API 응답에 맞게 수정
    }
}
