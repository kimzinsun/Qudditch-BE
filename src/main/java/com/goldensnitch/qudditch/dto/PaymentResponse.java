package com.goldensnitch.qudditch.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class PaymentResponse {
    private String tid; // 결제 고유 번호
    private String next_redirect_app_url; // 모바일 앱 Redirect URL
    private String next_redirect_mobile_url; // 모바일 웹 Redirect URL
    private String next_redirect_pc_url; // PC 웹 Redirect URL
    private String android_app_scheme; // Android 앱 스킴
    private String ios_app_scheme; // iOS 앱 스킴
    private LocalDateTime created_at; // 결제 준비 요청 시간
}


