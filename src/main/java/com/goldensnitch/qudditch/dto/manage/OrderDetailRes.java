package com.goldensnitch.qudditch.dto.manage;

import lombok.Data;

@Data
public class OrderDetailRes {
    private String brand;
    private String name;
    private int price;
    private int qty;
}
