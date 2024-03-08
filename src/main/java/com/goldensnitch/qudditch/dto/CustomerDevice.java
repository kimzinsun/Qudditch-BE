package com.goldensnitch.qudditch.dto;

import lombok.Data;

import java.sql.Date;

@Data
public class CustomerDevice {
    private Integer id;
    private Integer userCustomerId;
    private String token;
    private Date refreshedAt;
}
