package com.goldensnitch.qudditch.dto.RecommendOrder;

import com.goldensnitch.qudditch.dto.StoreStock;
import lombok.Data;

@Data
public class RecommendOrderReq extends StoreStock {
    private String brand;
    private String name;
    private String image;
    private Integer qty;
}
