package com.goldensnitch.qudditch.dto;

import lombok.Data;

import java.sql.Date;

@Data
public class StoreStockReport {
    private Integer id;
    private Integer userStoreId;
    private Integer productId;
    private Date ym;
    private Integer inQty;
    private Integer outQty;
    private Integer dspQty;
}
