package com.goldensnitch.qudditch.dto;

import lombok.Data;

@Data
public class UserAdmin {
    private Integer id;
    private String email;
    private String password;
    private String name;
    // 관리자가 가질 수 있는 추가적인 필드들
    // 예를 들어, 관리자 역할, 권한 등을 나타낼 수 있는 필드
}
