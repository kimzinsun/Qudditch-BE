package com.goldensnitch.qudditch.dto;

import lombok.Data;

@Data
public class CustomerOrderProduct {
    private Integer customerOrderId;
    private Integer productId;
    private Integer qty;
    private Integer price;

    // 추가
    private String productName;
}
