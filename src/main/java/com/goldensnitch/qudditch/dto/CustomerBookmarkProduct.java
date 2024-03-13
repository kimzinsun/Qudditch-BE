package com.goldensnitch.qudditch.dto;

import lombok.Data;

@Data
public class CustomerBookmarkProduct {
    private Integer id;
    private Integer userCustomerId;
    private Integer productId;
}
