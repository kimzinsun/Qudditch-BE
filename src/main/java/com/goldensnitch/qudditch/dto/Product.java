package com.goldensnitch.qudditch.dto;

import lombok.Data;

@Data
public class Product {
    private Integer id;
    private Integer categoryId;
    private String brand;
    private String name;
    private String description;
    private String image;
    private Integer price;
    private String unitPrice;
    private Integer expirationDate;
}


