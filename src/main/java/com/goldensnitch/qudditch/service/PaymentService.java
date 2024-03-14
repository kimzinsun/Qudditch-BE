package com.goldensnitch.qudditch.service;

import com.goldensnitch.qudditch.dto.PaymentRequest;
import com.goldensnitch.qudditch.dto.PaymentResponse;
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
    public String initiatePayment(PaymentRequest paymentRequest) {

        HttpHeaders headers = new HttpHeaders();

        // "Authorization" 헤더에 카카오페이 인증 키 추가
        headers.add("Authorization", "KakaoAK " + kakaoPayAuthorization);
        // 요청 본문의 "Content-Type"을 "application/x-www-form-urlencoded"로 설정
        // headers.add("Content-Type", "application/x-www-form-urlencoded;charset=utf-8");
        /*
            HTTP 요청 본문에 포함시키기 위한 HttpMessageConverter를 찾지 못했음을 나타냅니다.
            기본적으로, RestTemplate은 JSON 형식(application/json)의 데이터 전송에 사용되는
            HttpMessageConverter를 포함하고 있지만, application/x-www-form-urlencoded 형식으로
            데이터를 전송하기 위한 컨버터는 별도로 설정해야 합니다.
         */
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        /*
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
    */

        // PaymentRequest 객체 대신 MultiValueMap을 사용하여 요청 파라미터를 설정
        MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
        map.add("cid", paymentRequest.getCid());
        map.add("partner_order_id", paymentRequest.getPartner_order_id());
        map.add("partner_user_id", paymentRequest.getPartner_user_id());
        map.add("item_name", paymentRequest.getItem_name());
        map.add("quantity", paymentRequest.getQuantity().toString());
        map.add("total_amount", paymentRequest.getTotal_amount().toString());
        map.add("tax_free_amount", paymentRequest.getTax_free_amount().toString());
        map.add("approval_url", paymentRequest.getApproval_url());
        map.add("cancel_url", paymentRequest.getCancel_url());
        map.add("fail_url", paymentRequest.getFail_url());

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

        /*
        CustomerOrder customerOrder = new CustomerOrder();
        List<CustomerOrderProduct> customerOrderProducts = 결제된 상품 목록;
        customerOrderProductService.createCustomerOrder(customerOrder, customerOrderProducts);
        */
    }
}
