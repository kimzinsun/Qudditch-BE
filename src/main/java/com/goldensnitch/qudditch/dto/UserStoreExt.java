package com.goldensnitch.qudditch.dto;

import lombok.Data;

import java.util.Date;

@Data
public class UserStoreExt {
    private String storeName;
    private String phone;
    private String address;
    private int id;
    private String email;
    private String name;
    private Date createdAt;


}
