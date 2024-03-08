package com.goldensnitch.qudditch.dto;

import lombok.Data;

import java.sql.Date;

@Data
public class StoreVisitorLog {
    private Integer id;
    private Integer userStoreId;
    private Integer userCustomerId;
    private Date enteredAt;
}
