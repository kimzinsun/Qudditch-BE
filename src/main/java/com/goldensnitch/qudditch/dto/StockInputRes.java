package com.goldensnitch.qudditch.dto;

import lombok.Data;

import java.util.Date;

@Data
public class StockInputRes {
    private int id;
    private Date inputAt;
    private String items;

}
