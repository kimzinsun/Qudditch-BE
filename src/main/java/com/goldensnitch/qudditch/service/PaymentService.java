package com.goldensnitch.qudditch.service;

import com.goldensnitch.qudditch.dto.payment.PaymentResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

// https://developers.kakao.com/console/app/1046778/config/appKey
// https://jungkeung.tistory.com/149
@Service
public class PaymentService {
    private final RestTemplate restTemplate;

    // 카카오페이 결제 요청
    @Value("${kakao.pay.ready.url}") private String kakaoPayReadyUrl;
    // 결제 승인
    @Value("${kakao.pay.approve.url}") private String kakaoPayApproveUrl;
    // 결제 취소
    @Value("${kakao.pay.cancel.url}") private String kakaoPayCancelUrl;
    // 카카오페이 API 사용을 위한 인증 키
    @Value("${kakao.pay.authorization}") private String kakaoPayAuthorization;

    // RestTemplate 주입을 통한 HTTP 클라이언트 초기화
    @Autowired
    public PaymentService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    // 결제 준비를 시작하고 사용자를 결제 페이지로 리디렉션하는 URL을 반환하는 메소드
    public String initiatePayment(String cid, String partnerOrderId, String partnerUserId,
                                  String itemName, Integer quantity, Integer totalAmount,
                                  Integer taxFreeAmount, String approvalUrl, String cancelUrl, String failUrl) {
        HttpHeaders headers = new HttpHeaders();
        // "Authorization" 헤더에 카카오페이 인증 키 추가
        headers.add("Authorization", "KakaoAK " + kakaoPayAuthorization);
        // 요청 본문의 "Content-Type"을 "application/x-www-form-urlencoded"로 설정

        // headers.add("Content-Type", "application/x-www-form-urlencoded;charset=utf-8");
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        // PaymentRequest 객체 대신 MultiValueMap을 사용하여 요청 파라미터를 설정
        MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
        map.add("cid", cid);
        map.add("partner_order_id", partnerOrderId);
        map.add("partner_user_id", partnerUserId);
        map.add("item_name", itemName);
        map.add("quantity", quantity.toString());
        map.add("total_amount", totalAmount.toString());
        map.add("tax_free_amount", taxFreeAmount.toString());
        map.add("approval_url", "http://localhost:8080/approval");
        map.add("cancel_url", "http://localhost:8080/cancel");
        map.add("fail_url", "http://localhost:8080/fail");

        HttpEntity<MultiValueMap<String, String>> entity = new HttpEntity<>(map, headers);

        try {
            ResponseEntity<PaymentResponse> responseEntity = restTemplate.exchange(
                    kakaoPayReadyUrl, HttpMethod.POST, entity, PaymentResponse.class);

            PaymentResponse paymentResponse = responseEntity.getBody();
            if (paymentResponse != null) {
                return paymentResponse.getNext_redirect_pc_url();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "Error";
    }

    public PaymentResponse approvePayment(String tid, String pgToken) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "KakaoAK " + kakaoPayAuthorization);
        headers.setContentType(MediaType.APPLICATION_JSON);

        MultiValueMap<String, String> parameters = new LinkedMultiValueMap<>();
        parameters.add("cid", "TC0ONETIME"); // 가맹점 코드
        parameters.add("tid", tid); // 결제 고유 번호
        parameters.add("pg_token", pgToken); // 사용자 결제 인증 토큰

        HttpEntity<MultiValueMap<String, String>> entity = new HttpEntity<>(parameters, headers);
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<PaymentResponse> response = restTemplate.exchange(
                kakaoPayApproveUrl, HttpMethod.POST, entity, PaymentResponse.class);

        return response.getBody();
    }

    // 결제 취소 메서드
    public PaymentResponse cancelPayment(String tid, String cancelAmount, String cancelTaxFreeAmount) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "KakaoAK " + kakaoPayAuthorization);
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        MultiValueMap<String, String> parameters = new LinkedMultiValueMap<>();
        parameters.add("cid", "TC0ONETIME"); // 가맹점 코드
        parameters.add("tid", tid); // 결제 고유 번호
        parameters.add("cancel_amount", cancelAmount); // 취소 금액
        parameters.add("cancel_tax_free_amount", cancelTaxFreeAmount); // 비과세 금액

        HttpEntity<MultiValueMap<String, String>> entity = new HttpEntity<>(parameters, headers);

        ResponseEntity<PaymentResponse> response = restTemplate.exchange(
                kakaoPayCancelUrl, HttpMethod.POST, entity, PaymentResponse.class);

        return response.getBody();
    }
}
