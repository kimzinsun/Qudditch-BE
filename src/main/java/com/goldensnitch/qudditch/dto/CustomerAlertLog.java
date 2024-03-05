package com.goldensnitch.qudditch.dto;

import lombok.Data;

import java.sql.Date;

@Data
public class CustomerAlertLog {
    private Integer id;
    private Integer userId;
    private String title;
    private String body;
    private Object etc;
    private Date createdAt;
    private Date readedAt;
}
