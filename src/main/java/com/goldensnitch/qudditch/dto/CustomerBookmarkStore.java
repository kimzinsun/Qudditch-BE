package com.goldensnitch.qudditch.dto;

import lombok.Data;

@Data
public class CustomerBookmarkStore {
    private Integer id;
    private Integer customerId;
    private Integer storeId;
}
