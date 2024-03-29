package com.goldensnitch.qudditch.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class StoreQtyRes extends Store {
    private int qty;

}
