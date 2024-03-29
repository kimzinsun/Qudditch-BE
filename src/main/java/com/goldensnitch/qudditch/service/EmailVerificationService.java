package com.goldensnitch.qudditch.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class EmailVerificationService {

    @Autowired
    private RestTemplate restTemplate; // RestTemplate 빈을 주입받습니다.

    // application.properties에서 설정한 값들을 주입받습니다.
    @Value("${naver.api.client-id}")
    private String naverClientId;

    // application.properties에서 설정한 네이버 클라이언트 시크릿
    @Value("${naver.api.client-secret}")
    private String naverClientSecret;

    public void sendVerificationEmail(String userEmail, String verificationCode) {
        String apiUrl = "https://api.naver.com/email/send"; // 네이버 이메일 발송 API URL
        
        HttpHeaders headers = new HttpHeaders();
        headers.set("X-Naver-Client-Id", naverClientId);
        headers.set("X-Naver-Client-Secret", naverClientSecret);
        // 이메일 내용을 구성하는 부분입니다. 실제 구현 시에는 이메일 제목, 내용 등을 포함하여 구성해야 합니다.
        String emailContent = "여기에 이메일 본문을 구성합니다. 인증 코드: " + verificationCode;
        
        // HttpEntity를 사용하여 요청 본문과 헤더를 함께 전송합니다.
        HttpEntity<String> request = new HttpEntity<>(emailContent, headers);
        
        // RestTemplate을 사용하여 API를 호출하고 결과를 받습니다.
        String response = restTemplate.postForObject(apiUrl, request, String.class);
        
        // 응답 처리 로직을 구현합니다. 예시 코드에서는 단순히 응답을 콘솔에 출력하고 있습니다.
        System.out.println(response);
    }
}
