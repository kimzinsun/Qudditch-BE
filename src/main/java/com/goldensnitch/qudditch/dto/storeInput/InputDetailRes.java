package com.goldensnitch.qudditch.dto.storeInput;

import lombok.Data;

@Data
public class InputDetailRes {
    private int productId;
    private String brand;
    private String name;
    private int price;
    private int qty;
    private String state;
    private String expDate;
    private int locate;
}
