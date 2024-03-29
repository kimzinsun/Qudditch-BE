package com.goldensnitch.qudditch.dto.RecommendOrder;

import lombok.Data;

@Data
public class RecommendOrderReq{
    private Integer productId;
    private String brand;
    private String name;
    private String image;
    private Integer qty;
}
