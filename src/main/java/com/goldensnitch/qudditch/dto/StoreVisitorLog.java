package com.goldensnitch.qudditch.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.sql.Date;

@Data
@AllArgsConstructor
public class StoreVisitorLog {
    private Integer id;
    private Integer userStoreId;
    private Integer userCustomerId;
    private Date enteredAt;

    public StoreVisitorLog() {
    }

    public StoreVisitorLog(Integer userStoreId, Integer userId) {
        this.userStoreId = userStoreId;
        this.userCustomerId = userId;
    }

}
