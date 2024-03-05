package com.goldensnitch.qudditch.dto;

import lombok.Data;

import java.sql.Date;

@Data
public class StoreOrder {
    private Integer id;
    private Integer storeId;
    private Object state;
    private Integer totalAmount;
    private Date orderedAt;
}
