package com.goldensnitch.qudditch.dto;

import lombok.Data;

@Data
public class CustomerOrderProduct {
    private Integer customerOrderId;
    private Integer productId;
    private Integer qty;
}
