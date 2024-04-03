package com.goldensnitch.qudditch.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@Slf4j
@Service
public class NaverCloudOCRService implements OCRService {
    @Value("${naver.cloud.ocrApiUrl}")
    private String ocrApiUrl;
    @Value("${naver.cloud.ocrSecretKey}")
    private String ocrSecretKey;

    private final RestTemplate restTemplate;

    @Autowired
    public NaverCloudOCRService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @Override
    public String extractBusinessNumber(MultipartFile file) {
        // 파일 업로드 및 OCR 처리 로직 추가
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);
        headers.set("X-OCR-SECRET", ocrSecretKey);


        // Multipart 요청을 생성
        MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
        Map<String, Object> message = Map.of(
            "version", "V2",
            "requestId", UUID.randomUUID().toString(),
            "timestamp", System.currentTimeMillis(),
            "images", List.of(Map.of("format", "jpeg", "name", "file"))
        );

        body.add("message", message);
        body.add("file", file.getResource());

        HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(body, headers);
        ResponseEntity<String> response = restTemplate.postForEntity(ocrApiUrl, requestEntity, String.class);

        // OCR 응답에서 사업자 번호 추출 로직
        // 응답 구조에 따라 파싱 로직 구현
        return extractBusinessNumberFromResponse(response.getBody());
    }

    private String extractBusinessNumberFromResponse(String body) {
        // OCR 응답에서 사업자 번호 추출 로직
        // 실제 구현은 API 응답 구조에 따라 다를 수 있습니다.
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            Map<String, Object> response = objectMapper.readValue(body, Map.class);
            List<Map<String, Object>> images = (List<Map<String, Object>>) response.get("images");
            Map<String, Object> image = images.get(0);
            List<Map<String, Object>> fields = (List<Map<String, Object>>) image.get("fields");
            return (String) fields.get(0).get("inferText");
        } catch (Exception e) {
            log.error("Error while extracting business number from OCR response", e);
            return null;
        }
    }
}