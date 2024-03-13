package com.goldensnitch.qudditch.dto;

import lombok.Data;

import java.util.Map;

@Data
public class PaymentRequest {
    private String cid; // 가맹점 코드(10자)
    private String cid_secret; // 가맹점 코드 인증키(24자)
    private String partner_order_id; // 가맹점 주문번호(최대 100자)
    private String partner_user_id; // 가맹점 회원 id(최대 100자)
    private String item_name; // 상품명(최대 100자)
    private String item_code; // 상품코드(최대 100자)
    private Integer quantity; // 상품 수량
    private Integer total_amount; // 상품 총액
    private Integer tax_free_amount; // 상품 비과세 금액
    private Integer vat_amount; // 상품 부가세 금액: 값을 보내지 않을 경우 (상품총액-상품 비과세 금액)/1.1 소숫점 이하 반올림
    private Integer green_deposit; // 컵 보증금
    private String approval_url; // 결제 성공 시 redirect url, 최대 255자
    private String cancel_url; // 결제 취소 시 redirect url, 최대 255자
    private String fail_url; // 결제 실패 시 redirect url, 최대 255자
    private String[] available_cards; // 결제 수단으로써 허가할 카드사 지정 (기본값: 모든 카드사 허용)
    private String payment_method_type; // 사용 허가할 결제 수단
    private Integer install_month; // 카드 할부개월
    private String use_share_installment; // 분담무이자 설정
    private Map<String, String> custom_json; // 사전에 정의된 기능

    // 추가
    private Integer usedPoint; // 포인트 사용금액
    private Integer totalPay; // 실제 지불금액
    private Integer earnPoint; // 포인트 모으기
}


