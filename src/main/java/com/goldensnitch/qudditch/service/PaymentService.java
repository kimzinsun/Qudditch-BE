package com.goldensnitch.qudditch.service;

import com.goldensnitch.qudditch.dto.CustomerOrder;
import com.goldensnitch.qudditch.dto.payment.PaymentRequest;
import com.goldensnitch.qudditch.dto.payment.PaymentResponse;
import com.goldensnitch.qudditch.mapper.CustomerOrderProductMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

// https://developers.kakao.com/console/app/1046778/config/appKey
// https://jungkeung.tistory.com/149
@Service
public class PaymentService {
    private final RestTemplate restTemplate;
    private final String kakaoPayAuthorization;

    @Autowired
    private CustomerOrderProductMapper customerOrderProductMapper;

    // 카카오페이 결제 요청
    @Value("${kakao.pay.ready.url}")
    private String kakaoPayReadyUrl;
    // 결제 승인
    @Value("${kakao.pay.approve.url}")
    private String kakaoPayApproveUrl;
    // 결제 취소
    @Value("${kakao.pay.cancel.url}")
    private String kakaoPayCancelUrl;
    // 결제 실패
    @Value("${kakao.pay.fail.url}")
    private String kakaoPayFailUrl;

    // 카카오페이 API 사용을 위한 인증 키
//    @Value("${kakao.pay.secret.key.dev}")
//    private String kakaoPaySecretKey;
    // 가맹점 코드
    @Value("${kakao.pay.cid}")
    private String cid;

    // RestTemplate 주입을 통한 HTTP 클라이언트 초기화
    @Autowired
    public PaymentService(RestTemplate restTemplate, @Value("${kakao.pay.secret.key.dev}") String kakaoPaySecretKey) {
        this.restTemplate = restTemplate;
        this.kakaoPayAuthorization = "SECRET_KEY " + kakaoPaySecretKey;
    }

    // 결제 준비를 시작하고 사용자를 결제 페이지로 리디렉션하는 URL을 반환하는 메소드
    public String initiatePayment(PaymentRequest request) {
        HttpHeaders headers = new HttpHeaders();
        // "Authorization" 헤더에 카카오페이 인증 키 추가
        headers.add("Authorization", kakaoPayAuthorization);
        // 요청 본문의 "Content-Type"을 "application/x-www-form-urlencoded"로 설정
        headers.setContentType(MediaType.APPLICATION_JSON);

        // PaymentRequest 객체 대신 MultiValueMap을 사용하여 요청 파라미터를 설정
        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("cid", request.getCid());
        requestBody.put("partner_order_id", request.getPartner_order_id());
        requestBody.put("partner_user_id", request.getPartner_user_id());
        requestBody.put("item_name", request.getItem_name());
        requestBody.put("quantity", request.getQuantity());
        requestBody.put("total_amount", request.getTotal_amount());
        requestBody.put("tax_free_amount", request.getTax_free_amount());
        requestBody.put("approval_url", "http://localhost:8080/approve");
        requestBody.put("cancel_url", "http://localhost:8080/cancel");
        requestBody.put("fail_url", "http://localhost:8080/fail");

        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(requestBody, headers);

        try {
            ResponseEntity<PaymentResponse> responseEntity = restTemplate.exchange(
                    kakaoPayReadyUrl, HttpMethod.POST, entity, PaymentResponse.class);

            PaymentResponse response = responseEntity.getBody();

            System.out.println("initiatePayment 1");

            // paymentResopnse에서 tid 디비 저장
            if (response != null) {
                // 객체 생성 및 필요한 정보 설정
                CustomerOrder order = new CustomerOrder();
                order.setPartnerOrderId(Integer.valueOf(request.getPartner_order_id()));
                order.setUserCustomerId(2);
                order.setUserStoreId(Integer.valueOf(request.getPartner_user_id())); // 가맹점
                order.setTotalAmount(request.getTotal_amount());
                order.setUsedPoint(request.getUsedPoint() != null ? request.getUsedPoint() : 0);
                order.setTotalPay(request.getTotalPay() != null ? request.getTotalPay() : request.getTotal_amount()); // 예시로 total_amount를 기본값으로 사용
                order.setEarnPoint(request.getEarnPoint() != null ? request.getEarnPoint() : 0);
                order.setOrderedAt(LocalDateTime.now());
                order.setTid(response.getTid());
                order.setCid(request.getCid());

                System.out.println("initiatePayment 2");

                // DB 에 주문 정보 저장
                customerOrderProductMapper.insertCustomerOrder(order);

                System.out.println("initiatePayment 3");
                return response.getNext_redirect_pc_url();
            }
        } catch (Exception e) {
            System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~paymentResopnse에서 Exception");
            e.printStackTrace();
        }
        return "error";
    }

    public PaymentResponse approvePayment(String pgToken, String partnerOrderId) throws Exception {

        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", kakaoPayAuthorization);
        headers.setContentType(MediaType.APPLICATION_JSON);

        System.out.println("~~~~~~~~~~~~~~~~~~~approvePayment 1");

        CustomerOrder order = customerOrderProductMapper.findByPartnerOrderId(partnerOrderId);
        if (order == null) {
            throw new Exception("Order not found with partner_order_id: " + partnerOrderId);
        }

        System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~approvePayment 2");

        // JSON 형식으로 요청 바디 구성
        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("cid", order.getCid());
        requestBody.put("tid", order.getTid());
        requestBody.put("pg_token", pgToken);
        requestBody.put("partner_order_id", order.getPartnerOrderId());
        requestBody.put("partner_user_id", order.getUserStoreId());

        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(requestBody, headers);
        ResponseEntity<PaymentResponse> responseEntity = restTemplate.exchange(
                kakaoPayApproveUrl, HttpMethod.POST, entity, PaymentResponse.class);

        System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~approvePayment 3");

        return responseEntity.getBody();
    }

    // 결제 취소 메서드
    public PaymentResponse cancelPayment(String tid, String cancelAmount, String cancelTaxFreeAmount) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", kakaoPayAuthorization);
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        MultiValueMap<String, String> parameters = new LinkedMultiValueMap<>();
        parameters.add("cid", cid); // 가맹점 코드
        parameters.add("tid", tid); // 결제 고유 번호
        parameters.add("cancel_amount", cancelAmount); // 취소 금액
        parameters.add("cancel_tax_free_amount", cancelTaxFreeAmount); // 비과세 금액

        HttpEntity<MultiValueMap<String, String>> entity = new HttpEntity<>(parameters, headers);

        ResponseEntity<PaymentResponse> response = restTemplate.exchange(
                kakaoPayCancelUrl, HttpMethod.POST, entity, PaymentResponse.class);

        return response.getBody();
    }
}
