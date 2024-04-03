package com.goldensnitch.qudditch.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class StoreStockRes extends StoreStock{
    private String categoryName;
    private String brand;
    private String productName;
    private String productImage;
    private int productPrice;


}
