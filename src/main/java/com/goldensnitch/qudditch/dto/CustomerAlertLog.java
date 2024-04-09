package com.goldensnitch.qudditch.dto;

import lombok.Data;

import java.sql.Date;

@Data
public class CustomerAlertLog {
    private Integer id;
    private Integer userCustomerId;
    private String title;
    private String body;
    private Object etc;
    private String createdAt;
    private String readedAt;
}
