package com.goldensnitch.qudditch.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor // 모든 인자를 가진 생성자를 자동으로 생성합니다.
public class AuthResponse {
    private String token;
}
