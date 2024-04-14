package com.goldensnitch.qudditch.dto.payment;

import lombok.Data;

@Data
public class OrderProductStoreInfo {
    private Integer productId;
    private Integer qty;
    private Integer productPrice;
    private String productName;
    private Integer storeId;
    private String storeName;
    private String storeAddress;
    private String storePhone;
    private String businessNumber;
}