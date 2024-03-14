package com.goldensnitch.qudditch.dto.storeInput;

import lombok.Data;

import java.util.Date;

@Data
public class InputRepoReq {
    private int userStoreId;
    private int productId;
    private Date ymd;
    private int inQty;
}
