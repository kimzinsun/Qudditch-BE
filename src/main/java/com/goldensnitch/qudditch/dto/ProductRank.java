package com.goldensnitch.qudditch.dto;

import lombok.Data;

@Data
public class ProductRank {
    private int rank;
    private int productId;
    private String productName;
    private String productImage;
    private int price;
}
