package com.goldensnitch.qudditch.dto;

import lombok.Data;

@Data
public class StoreLocQty {
    private Integer id;
    private String name;
    private double wgs84X;
    private double wgs84Y;
    private int qty;
}
