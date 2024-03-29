package com.goldensnitch.qudditch.dto;

import lombok.Data;

@Data
public class SocialLoginDto {

    private String email;
    private String name;
    private String naverId; // 네이버 사용자 고유 ID
}
