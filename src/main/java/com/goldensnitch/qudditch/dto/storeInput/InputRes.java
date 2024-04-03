package com.goldensnitch.qudditch.dto.storeInput;

import lombok.Data;

import java.util.Date;

@Data
public class InputRes {
    private int storeInputId;
    private String items;
    private Date inputAt;
    private String state;
}
