package com.goldensnitch.qudditch.dto;

import lombok.Data;

import java.sql.Date;

@Data
public class UserStore {
    private Integer id;
    private String email;
    private String password;
    private String name;
    private String storeName;
    private String phone;
    private String address;
    private Integer bnNumber;
    private Double wgs84X;
    private Double wgs84Y;
    /**
     * 유저 상태 0: 미인증, 1: 일반, 2: 탈퇴
     */
    private Integer state;
    private Date createdAt;
    private Date modifiedAt;
}
