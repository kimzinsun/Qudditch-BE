package com.goldensnitch.qudditch.dto;

import lombok.Data;

import java.sql.Date;

@Data
public class DisposalItem {
    private int id; // storeStockId
    private int userStoreId;
    private int positionId;
    private Date expiredAt;
    private int qty;
    private int productId;
    private String productName;
    private String brand;
    private int productPrice;
}
