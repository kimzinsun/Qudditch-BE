package com.goldensnitch.qudditch.dto;

import lombok.Data;

import java.sql.Date;

@Data
public class UserCustomer {
    private Integer id;
    private String email;
    private String password;
    private String name;
    /**
     * 유저 상태 0: 미인증, 1: 일반, 2: 탈퇴
     */
    private Integer state;
    private String collectionArn;
    private Date createdAt;
    private Date modifiedAt;
}
