package com.goldensnitch.qudditch.dto;

import lombok.Data;

import java.sql.Date;

@Data
public class StoreDailySales {

    private String name;
    private Integer totalAmount;
    private Date orderedAt;
}
