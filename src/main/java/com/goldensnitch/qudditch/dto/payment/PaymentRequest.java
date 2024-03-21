package com.goldensnitch.qudditch.dto.payment;

import java.util.Map;

//@Data
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

    public PaymentRequest() {
    }

    public PaymentRequest(String cid, String cid_secret, String partner_order_id, String partner_user_id, String item_name, String item_code, Integer quantity, Integer total_amount, Integer tax_free_amount, Integer vat_amount, Integer green_deposit, String approval_url, String cancel_url, String fail_url, String[] available_cards, String payment_method_type, Integer install_month, String use_share_installment, Map<String, String> custom_json, Integer usedPoint, Integer totalPay, Integer earnPoint) {
        this.cid = cid;
        this.cid_secret = cid_secret;
        this.partner_order_id = partner_order_id;
        this.partner_user_id = partner_user_id;
        this.item_name = item_name;
        this.item_code = item_code;
        this.quantity = quantity;
        this.total_amount = total_amount;
        this.tax_free_amount = tax_free_amount;
        this.vat_amount = vat_amount;
        this.green_deposit = green_deposit;
        this.approval_url = approval_url;
        this.cancel_url = cancel_url;
        this.fail_url = fail_url;
        this.available_cards = available_cards;
        this.payment_method_type = payment_method_type;
        this.install_month = install_month;
        this.use_share_installment = use_share_installment;
        this.custom_json = custom_json;
        this.usedPoint = usedPoint;
        this.totalPay = totalPay;
        this.earnPoint = earnPoint;
    }

    public String getCid() {
        return cid;
    }

    public void setCid(String cid) {
        this.cid = cid;
    }

    public String getCid_secret() {
        return cid_secret;
    }

    public void setCid_secret(String cid_secret) {
        this.cid_secret = cid_secret;
    }

    public String getPartner_order_id() {
        return partner_order_id;
    }

    public void setPartner_order_id(String partner_order_id) {
        this.partner_order_id = partner_order_id;
    }

    public String getPartner_user_id() {
        return partner_user_id;
    }

    public void setPartner_user_id(String partner_user_id) {
        this.partner_user_id = partner_user_id;
    }

    public String getItem_name() {
        return item_name;
    }

    public void setItem_name(String item_name) {
        this.item_name = item_name;
    }

    public String getItem_code() {
        return item_code;
    }

    public void setItem_code(String item_code) {
        this.item_code = item_code;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public Integer getTotal_amount() {
        return total_amount;
    }

    public void setTotal_amount(Integer total_amount) {
        this.total_amount = total_amount;
    }

    public Integer getTax_free_amount() {
        return tax_free_amount;
    }

    public void setTax_free_amount(Integer tax_free_amount) {
        this.tax_free_amount = tax_free_amount;
    }

    public Integer getVat_amount() {
        return vat_amount;
    }

    public void setVat_amount(Integer vat_amount) {
        this.vat_amount = vat_amount;
    }

    public Integer getGreen_deposit() {
        return green_deposit;
    }

    public void setGreen_deposit(Integer green_deposit) {
        this.green_deposit = green_deposit;
    }

    public String getApproval_url() {
        return approval_url;
    }

    public void setApproval_url(String approval_url) {
        this.approval_url = approval_url;
    }

    public String getCancel_url() {
        return cancel_url;
    }

    public void setCancel_url(String cancel_url) {
        this.cancel_url = cancel_url;
    }

    public String getFail_url() {
        return fail_url;
    }

    public void setFail_url(String fail_url) {
        this.fail_url = fail_url;
    }

    public String[] getAvailable_cards() {
        return available_cards;
    }

    public void setAvailable_cards(String[] available_cards) {
        this.available_cards = available_cards;
    }

    public String getPayment_method_type() {
        return payment_method_type;
    }

    public void setPayment_method_type(String payment_method_type) {
        this.payment_method_type = payment_method_type;
    }

    public Integer getInstall_month() {
        return install_month;
    }

    public void setInstall_month(Integer install_month) {
        this.install_month = install_month;
    }

    public String getUse_share_installment() {
        return use_share_installment;
    }

    public void setUse_share_installment(String use_share_installment) {
        this.use_share_installment = use_share_installment;
    }

    public Map<String, String> getCustom_json() {
        return custom_json;
    }

    public void setCustom_json(Map<String, String> custom_json) {
        this.custom_json = custom_json;
    }

    public Integer getUsedPoint() {
        return usedPoint;
    }

    public void setUsedPoint(Integer usedPoint) {
        this.usedPoint = usedPoint;
    }

    public Integer getTotalPay() {
        return totalPay;
    }

    public void setTotalPay(Integer totalPay) {
        this.totalPay = totalPay;
    }

    public Integer getEarnPoint() {
        return earnPoint;
    }

    public void setEarnPoint(Integer earnPoint) {
        this.earnPoint = earnPoint;
    }
}
