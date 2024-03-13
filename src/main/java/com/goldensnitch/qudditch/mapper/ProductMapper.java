package com.goldensnitch.qudditch.mapper;

import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface ProductMapper {
    void insertProduct(Integer categoryId, String brand, String name, String description, String image, Integer price, String unitPrice, Integer expirationDate);

}
