package com.goldensnitch.qudditch.dto.payment;

import java.time.LocalDateTime;

// @Data
public class PaymentResponse {
    private String cid; // 가맹점 코드
    private String tid; // 결제 고유 번호
    private String next_redirect_app_url; // 모바일 앱 Redirect URL
    private String next_redirect_mobile_url; // 모바일 웹 Redirect URL
    private String next_redirect_pc_url; // PC 웹 Redirect URL
    private String android_app_scheme; // Android 앱 스킴
    private String ios_app_scheme; // iOS 앱 스킴
    private LocalDateTime created_at; // 결제 준비 요청 시간
    private String pg_token;

    // 추가 03.20
    private String partner_order_id; // 가맹점 주문번호(최대 100자)
    private String partner_user_id; // 가맹점 회원 id(최대 100자)

    public  PaymentResponse(){
    }

    public PaymentResponse(String cid, String tid, String next_redirect_app_url, String next_redirect_mobile_url, String next_redirect_pc_url, String android_app_scheme, String ios_app_scheme, LocalDateTime created_at, String pg_token, String partner_order_id, String partner_user_id) {
        this.cid = cid;
        this.tid = tid;
        this.next_redirect_app_url = next_redirect_app_url;
        this.next_redirect_mobile_url = next_redirect_mobile_url;
        this.next_redirect_pc_url = next_redirect_pc_url;
        this.android_app_scheme = android_app_scheme;
        this.ios_app_scheme = ios_app_scheme;
        this.created_at = created_at;
        this.pg_token = pg_token;
        this.partner_order_id = partner_order_id;
        this.partner_user_id = partner_user_id;
    }

    public String getCid() {
        return cid;
    }

    public void setCid(String cid) {
        this.cid = cid;
    }

    public String getTid() {
        return tid;
    }

    public void setTid(String tid) {
        this.tid = tid;
    }

    public String getNext_redirect_app_url() {
        return next_redirect_app_url;
    }

    public void setNext_redirect_app_url(String next_redirect_app_url) {
        this.next_redirect_app_url = next_redirect_app_url;
    }

    public String getNext_redirect_mobile_url() {
        return next_redirect_mobile_url;
    }

    public void setNext_redirect_mobile_url(String next_redirect_mobile_url) {
        this.next_redirect_mobile_url = next_redirect_mobile_url;
    }

    public String getNext_redirect_pc_url() {
        return next_redirect_pc_url;
    }

    public void setNext_redirect_pc_url(String next_redirect_pc_url) {
        this.next_redirect_pc_url = next_redirect_pc_url;
    }

    public String getAndroid_app_scheme() {
        return android_app_scheme;
    }

    public void setAndroid_app_scheme(String android_app_scheme) {
        this.android_app_scheme = android_app_scheme;
    }

    public String getIos_app_scheme() {
        return ios_app_scheme;
    }

    public void setIos_app_scheme(String ios_app_scheme) {
        this.ios_app_scheme = ios_app_scheme;
    }

    public LocalDateTime getCreated_at() {
        return created_at;
    }

    public void setCreated_at(LocalDateTime created_at) {
        this.created_at = created_at;
    }

    public String getPg_token() {
        return pg_token;
    }

    public void setPg_token(String pg_token) {
        this.pg_token = pg_token;
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
}


