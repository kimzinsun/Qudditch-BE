package com.goldensnitch.qudditch.dto.manage;

import lombok.Data;

@Data
public class InputReq {
    private int productId;
    private int qty;
    private int expirationDate;
}
