package com.goldensnitch.qudditch.dto;

import lombok.Data;

import java.sql.Date;

@Data
public class StoreSalesDaily {
    private Integer id;
    private Integer userStoreId;
    private Date day;
    private Integer sales;
}
