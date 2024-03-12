package com.goldensnitch.qudditch.service;

import com.goldensnitch.qudditch.dto.PaymentRequest;
import com.goldensnitch.qudditch.dto.PaymentResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

// https://developers.kakao.com/console/app/1046778/config/appKey
// https://jungkeung.tistory.com/149

@Service
public class PaymentService {
    private final RestTemplate restTemplate;

    // kakao.pay.ready.url
    // 카카오페이 결제 준비 API의 엔드포인트 URL
    @Value("${kakao.pay.ready.url}")
    private String kakaoPayReadyUrl;

    // 카카오페이 API 사용을 위한 인증 키
    @Value("${kakao.pay.authorization}")
    private String kakaoPayAuthorization;

    // RestTemplate 주입을 통한 HTTP 클라이언트 초기화
    @Autowired
    public PaymentService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    // 결제 준비를 시작하고 사용자를 결제 페이지로 리디렉션하는 URL을 반환하는 메소드
    public String initiatePayment(String cid, String partnerOrderId, String partnerUserId,
                                  String itemName, Integer quantity, Integer totalAmount,
                                  Integer taxFreeAmount, String approvalUrl, String cancelUrl,
                                  String failUrl) {
        HttpHeaders headers = new HttpHeaders();

        // "Authorization" 헤더에 카카오페이 인증 키 추가
        headers.add("Authorization", "KakaoAK " + kakaoPayAuthorization);
        // 요청 본문의 "Content-Type"을 "application/x-www-form-urlencoded"로 설정
        headers.add("Content-Type", "application/x-www-form-urlencoded;charset=utf-8");

        // 결제 정보를 담는 PaymentRequest 객체 생성 및 초기화
        PaymentRequest paymentRequest = new PaymentRequest();
        paymentRequest.setCid(cid);
        paymentRequest.setPartner_order_id(partnerOrderId);
        paymentRequest.setPartner_user_id(partnerUserId);
        paymentRequest.setItem_name(itemName);
        paymentRequest.setQuantity(quantity);
        paymentRequest.setTotal_amount(totalAmount);
        paymentRequest.setTax_free_amount(taxFreeAmount);
        paymentRequest.setApproval_url(approvalUrl);
        paymentRequest.setCancel_url(cancelUrl);
        paymentRequest.setFail_url(failUrl);

        // 카카오페이 결제 준비 요청 URL, 테스트를 위한 가상 URL
        String prepareUrl = kakaoPayReadyUrl; // 예시: "https://kapi.kakao.com/v1/payment/ready"

        // 결제 준비 요청을 위한 HttpEntity 생성
        HttpEntity<PaymentRequest> entity = new HttpEntity<>(paymentRequest, headers);

        // 카카오페이 결제 준비 API 호출 및 응답 처리
        try {
            ResponseEntity<PaymentResponse> responseEntity = restTemplate.exchange(
                    kakaoPayReadyUrl, HttpMethod.POST, entity, PaymentResponse.class);

            PaymentResponse paymentResponse = responseEntity.getBody();
            if (paymentResponse != null) {
                // 사용자를 리디렉션할 결제 페이지 URL 반환
                return paymentResponse.getNext_redirect_pc_url();
            }
        } catch (Exception e) {
            // 오류 처리 로직
            e.printStackTrace();
        }
        // 오류 처리 로직, 적절한 오류 메시지나 페이지 URL 반환
        return "Error";
    }
}
