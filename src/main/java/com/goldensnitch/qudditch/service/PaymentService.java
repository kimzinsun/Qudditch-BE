package com.goldensnitch.qudditch.service;

import com.goldensnitch.qudditch.dto.CustomerOrder;
import com.goldensnitch.qudditch.dto.CustomerOrderProduct;
import com.goldensnitch.qudditch.dto.payment.CartItem;
import com.goldensnitch.qudditch.dto.payment.PaymentRequest;
import com.goldensnitch.qudditch.dto.payment.PaymentResponse;
import com.goldensnitch.qudditch.mapper.CustomerOrderProductMapper;
import com.goldensnitch.qudditch.mapper.StoreStockMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.sql.Date;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

// https://developers.kakao.com/console/app/1046778/config/appKey
// https://jungkeung.tistory.com/149
@Service
public class PaymentService {
    private final RestTemplate restTemplate;
    private final String kakaoPayAuthorization;

    @Autowired
    private StoreStockMapper storeStockMapper;

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
    public String initiatePayment(List<CartItem> cartItems, Integer userCustomerId) {
        HttpHeaders headers = new HttpHeaders();
        // "Authorization" 헤더에 카카오페이 인증 키 추가
        headers.add("Authorization", kakaoPayAuthorization);
        // 요청 본문의 "Content-Type"을 "application/x-www-form-urlencoded"로 설정
        headers.setContentType(MediaType.APPLICATION_JSON);

        PaymentRequest request = createPaymentRequest(cartItems);
        HttpEntity<PaymentRequest> entity = new HttpEntity<>(request, headers);

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
                order.setUserCustomerId(userCustomerId);
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

                // 생성된 주문 ID를 사용하여 CustomerOrderProduct에 품목 추가
                for (CartItem item : cartItems) { // cartItems는 주문에 포함된 품목의 목록
                    CustomerOrderProduct orderProduct = new CustomerOrderProduct();
                    orderProduct.setCustomerOrderId(order.getId()); // insertCustomerOrder 후 생성된 주문 ID
                    orderProduct.setProductId(item.getProductId());
                    orderProduct.setQty(item.getQty());

                    customerOrderProductMapper.insertCustomerOrderProduct(orderProduct);
                }

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

        System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~approvePayment 1");

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
        // 결제 승인이 성공한 후의 로직
        // 결제 승인 후 재고 차감 및 로그 기록을 위한 메서드 호출
        if (responseEntity.getStatusCode() == HttpStatus.OK) {
            decrementStockAndUpdateReport(order.getId());
            System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~approvePayment 4");
        }
        return responseEntity.getBody();
    }

    // 결제 취소 메서드
    public PaymentResponse cancelPayment(String tid, String cancelAmount, String cancelTaxFreeAmount) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", kakaoPayAuthorization);
        headers.setContentType(MediaType.APPLICATION_JSON);

        // 요청 본문을 JSON 객체로 생성
        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("cid", cid);
        requestBody.put("tid", tid);
        requestBody.put("cancel_amount", Integer.parseInt(cancelAmount));
        requestBody.put("cancel_tax_free_amount", Integer.parseInt(cancelTaxFreeAmount));

        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(requestBody, headers);

        ResponseEntity<PaymentResponse> response = restTemplate.exchange(
                kakaoPayCancelUrl, HttpMethod.POST, entity, PaymentResponse.class);

        if (response.getStatusCode() == HttpStatus.OK) {
            // 결제 취소 성공 시, 재고 복구 로직 실행
            CustomerOrder order = customerOrderProductMapper.findByTid(tid);
            if (order != null) {
                List<CustomerOrderProduct> orderProducts = customerOrderProductMapper.findOrderProductsByOrderId(order.getId());
                for (CustomerOrderProduct orderProduct : orderProducts) {
                    // 해당 상품의 현재 재고량 조회
                    Integer currentStock = storeStockMapper.selectStockQtyByProductIdAndUserStoreId(orderProduct.getProductId(), order.getUserStoreId());
                    // 재고량 복구
                    storeStockMapper.updateStockQtyByProductIdAndUserStoreId(orderProduct.getProductId(), order.getUserStoreId(), currentStock + orderProduct.getQty());

                    // 판매 정보 업데이트: 취소된 수량만큼 out_qty를 감소
                    LocalDate ymd = order.getOrderedAt().toLocalDate();
                    storeStockMapper.updateStoreStockReportOutQty(order.getUserStoreId(), orderProduct.getProductId(), Date.valueOf(ymd), -orderProduct.getQty());
                }
            }
        }

        return response.getBody();
    }

    // 결제 정보 조회 메서드
    public PaymentResponse getOrderInfo(String tid) throws Exception {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", kakaoPayAuthorization);
        headers.setContentType(MediaType.APPLICATION_JSON);

        // 요청 본문에 포함할 정보를 구성합니다.
        Map<String, String> requestBody = new HashMap<>();
        requestBody.put("cid", this.cid); // 가맹점 코드
        requestBody.put("tid", tid);      // 결제 고유 번호

        // HTTP 요청을 구성합니다.
        HttpEntity<Map<String, String>> entity = new HttpEntity<>(requestBody, headers);

        // 카카오페이 주문 조회 API에 요청을 보냅니다.
        ResponseEntity<PaymentResponse> responseEntity = restTemplate.exchange(
                "https://open-api.kakaopay.com/online/v1/payment/order",
                HttpMethod.POST,
                entity,
                PaymentResponse.class
        );

        // 응답을 처리합니다.
        if (responseEntity.getStatusCode() == HttpStatus.OK) {
            return responseEntity.getBody();
        } else {
            throw new Exception("Failed to retrieve order information");
        }
    }

    private int calculateTotalAmount(List<CartItem> cartItems) {
        return cartItems.stream()
                .mapToInt(item -> item.getPrice() * item.getQty())
                .sum();
    }

    private PaymentRequest createPaymentRequest(List<CartItem> cartItems) {
        Random rand = new Random();

        // 카트 아이템 정보를 바탕으로 결제에 필요한 정보 계산 및 설정
        String partner_user_id = String.valueOf(cartItems.get(0).getUserStoreId());
        String item_name = cartItems.get(0).getName();
        String item_code = String.valueOf(cartItems.get(0).getProductId());
        int random_order = rand.nextInt(90000000) + 10000000;
        Integer quantity = cartItems.get(0).getQty();
        Integer total_amount = calculateTotalAmount(cartItems);
        Integer vat_amount = (int) Math.round(total_amount * 0.1);

        // 결제 요청을 위한 PaymentRequest 객체 생성
        PaymentRequest paymentRequest = new PaymentRequest();

        // 컨트롤러에 설정한 파라미터가 paymentService 메소드로 전달되어 기능 동작
        paymentRequest.setCid("TC0ONETIME");
        paymentRequest.setPartner_order_id(String.valueOf(random_order)); // 고유한 주문 ID 생성
        paymentRequest.setPartner_user_id(partner_user_id);
        paymentRequest.setItem_name(item_name.length() > 80 ? item_name.substring(0, 77) + "..." : item_name); // 상품명 설정
        paymentRequest.setItem_code(item_code);
        paymentRequest.setQuantity(quantity);
        paymentRequest.setTotal_amount(total_amount);
        paymentRequest.setTax_free_amount(0);
        paymentRequest.setVat_amount(vat_amount);
        paymentRequest.setApproval_url("http://localhost:8080/approve");
        paymentRequest.setCancel_url("http://localhost:8080/cancel");
        paymentRequest.setFail_url("http://localhost:8080/fail");
//        paymentRequest.setUsedPoint(usedPoint);
//        paymentRequest.setTotalPay(계산된 최종 결제액);
//        paymentRequest.setEarnPoint(계산된 적립 포인트);

        return paymentRequest;
    }

    private void decrementStockAndUpdateReport(int partnerOrderId) {
        try {
            // 주문 정보 조회
            CustomerOrder order = customerOrderProductMapper.findById(partnerOrderId);
            if (order == null) {
                throw new RuntimeException("Order not found with ID: " + partnerOrderId);
            }

            Integer userStoreId = order.getUserStoreId();
            LocalDate ymd = order.getOrderedAt().toLocalDate();

            // 주문된 상품 정보 조회
            List<CustomerOrderProduct> orderProducts = customerOrderProductMapper.findOrderProductsByOrderId(partnerOrderId);
            if (orderProducts.isEmpty()) {
                throw new RuntimeException("No products found for order ID: " + partnerOrderId);
            }

            for (CustomerOrderProduct orderProduct : orderProducts) {
                // 현재 재고 조회
                Integer currentStoreStock = storeStockMapper.selectStockQtyByProductIdAndUserStoreId(orderProduct.getProductId(), userStoreId);
                if (currentStoreStock == null || currentStoreStock < orderProduct.getQty()) {
                    throw new RuntimeException("Insufficient stock for product ID: " + orderProduct.getProductId());
                }

                // 재고 차감
                Integer newStoreStock = currentStoreStock - orderProduct.getQty();
                storeStockMapper.updateStockQtyByProductIdAndUserStoreId(orderProduct.getProductId(), userStoreId, newStoreStock);

                // 판매 정보 업데이트 또는 삽입
                updateOrInsertStoreStockReport(userStoreId, orderProduct.getProductId(), ymd, orderProduct.getQty());
            }
        } catch (Exception e) {
            // 에러 로깅
            System.out.println("Error during decrementStockAndUpdateReport: " + e.getMessage());
            e.printStackTrace(); // 스택 트레이스를 콘솔에 출력
            throw e; // 예외를 다시 throw하여 호출자에게 전달
        }
    }

    private void updateOrInsertStoreStockReport(Integer userStoreId, Integer productId, LocalDate ymd, Integer outQty) {
        try {
            // 판매 수량 업데이트 시도
            int updatedRows = storeStockMapper.updateStoreStockReportOutQty(userStoreId, productId, Date.valueOf(ymd), outQty);
            if (updatedRows == 0) {
                // 업데이트된 행이 없으면 새로운 레코드 추가
                storeStockMapper.insertStoreStockReport(userStoreId, productId, Date.valueOf(ymd), outQty);
            }
        } catch(Exception e){
            // 에러 로깅
            System.out.println("Error during updateOrInsertStoreStockReport: " + e.getMessage());
            e.printStackTrace(); // 스택 트레이스를 콘솔에 출력
            throw e; // 예외를 다시 throw하여 호출자에게 전달
        }
    }
}
