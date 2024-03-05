package com.goldensnitch.qudditch.dto;

import lombok.Data;

@Data
public class StoreOrderProduct {
    private Integer orderStoreId;
    private Integer productId;
    private Integer qty;
}
