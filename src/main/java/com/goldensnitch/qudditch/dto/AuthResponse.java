package com.goldensnitch.qudditch.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor // 모든 인자를 가진 생성자를 자동으로 생성합니다.
public class AuthResponse {
    private String token;
}
