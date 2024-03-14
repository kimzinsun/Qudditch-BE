package com.goldensnitch.qudditch.dto;

import lombok.Data;

@Data
public class StockUpdateReq {
    Integer productId;
    Integer quantity;
    Integer positionId;
}
