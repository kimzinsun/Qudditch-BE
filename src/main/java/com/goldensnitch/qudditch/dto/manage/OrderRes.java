package com.goldensnitch.qudditch.dto.manage;

import lombok.Data;

import java.util.Date;

@Data
public class OrderRes {
    private int id;
    private Date orderedAt;
    private String name;
    private String items;
    private String state;

}
