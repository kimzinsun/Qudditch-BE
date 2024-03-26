package com.goldensnitch.qudditch.dto.manage;

import lombok.Data;
import org.joda.time.DateTime;

import java.sql.Date;
import java.time.LocalDate;

@Data
public class InputOrder{
    private int storeInputId;
    private int productId;
    private String expirationDate;
    private int qty;

}
