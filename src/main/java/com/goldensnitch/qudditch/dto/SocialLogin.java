package com.goldensnitch.qudditch.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class SocialLogin {

    private String token;   //토큰저장
    private String email;   // 이메일저장
    private String name;    // 이름
    private String regType; // 카카오, 구글, 네이버

    public SocialLogin(String token, String email, String name, String regType) {
        this.token = token;
        this.email = email;
        this.name = name;
        this.regType = regType;
    }
}
