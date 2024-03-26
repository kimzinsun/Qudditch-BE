package com.goldensnitch.qudditch.dto;

import lombok.Data;

import java.sql.Date;

@Data
public class CustomerOrder {
    private Integer id;
    private Integer userCustomerId;
    private Integer userStoreId;
    private Integer totalAmount;
    private Integer usedPoint;
    private Integer totalPay;
    private Integer earnPoint;
    private Date orderedAt;
    private String tid;
    private String yearMonth;
}
