package com.goldensnitch.qudditch.dto;

import lombok.Data;

import java.io.Serializable;
@Data
public class BestProduct implements Serializable {

    private String name;
    private Integer outQty;
}
