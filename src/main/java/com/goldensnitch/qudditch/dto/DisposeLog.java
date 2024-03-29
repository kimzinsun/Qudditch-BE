package com.goldensnitch.qudditch.dto;

import lombok.Data;

import java.util.Date;

@Data
public class DisposeLog {
    private int id;
    private int userStoreId;
    private int productId;
    private int qty;
    private Date disposalAt;
}
