package com.goldensnitch.qudditch.dto;
import lombok.Data;

import java.io.Serializable;

@Data
public class MonthSales implements Serializable{
    private String Month;
    private Integer Price;
    private String name;

}
