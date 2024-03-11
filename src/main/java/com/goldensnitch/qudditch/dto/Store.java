package com.goldensnitch.qudditch.dto;

import lombok.Data;

@Data
public class Store {
    private Integer id;
    private String name;
    private double wgs84X;
    private double wgs84Y;
    private String phone;
    private String address;

    private double currentWgs84X;
    private double currentWgs84Y;
}
