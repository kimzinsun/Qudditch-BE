package com.goldensnitch.qudditch.dto;

import lombok.Data;

import java.sql.Date;

@Data
public class StoreStock {
    private Integer id;
    private Integer userStoreId;
    private Integer productId;
    private Integer positionId;
    private Date receiptAt;
    private Date expiredAt;
    private Integer qty;
    private String positioned;
}
