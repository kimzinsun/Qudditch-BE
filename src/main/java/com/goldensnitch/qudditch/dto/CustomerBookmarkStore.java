package com.goldensnitch.qudditch.dto;

import lombok.Data;

@Data
public class CustomerBookmarkStore {
    private Integer id;
    private Integer userCustomerId;
    private Integer userStoreId;

    public CustomerBookmarkStore(int userId, Integer storeId) {
        this.userCustomerId = userId;
        this.userStoreId = storeId;
    }
}
