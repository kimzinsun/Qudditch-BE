package com.goldensnitch.qudditch.dto;

import lombok.Data;

import java.sql.Date;

@Data
public class StoreStock {
    private Integer id;
    private Integer storeId;
    private Integer productId;
    private Date receiptAt;
    private Date expiredAt;
    private Integer qty;
}
