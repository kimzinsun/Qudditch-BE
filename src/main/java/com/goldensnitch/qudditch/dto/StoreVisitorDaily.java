package com.goldensnitch.qudditch.dto;

import lombok.Data;

import java.sql.Date;

@Data
public class StoreVisitorDaily {
    private Integer id;
    private Integer storeId;
    private Date date;
    private Integer cnt;
}
