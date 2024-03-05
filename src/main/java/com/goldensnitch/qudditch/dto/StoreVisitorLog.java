package com.goldensnitch.qudditch.dto;

import lombok.Data;

import java.sql.Date;

@Data
public class StoreVisitorLog {
    private Integer id;
    private Integer storeId;
    private Integer customerId;
    private Date enteredAt;
}
