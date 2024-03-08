package com.goldensnitch.qudditch.mapper;

import com.goldensnitch.qudditch.dto.Product;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface ProductMapper {
    void insertProduct(Integer categoryId, String brand, String name, String description, String image, Integer price, String unitPrice, Integer expirationDate);

    // 규원 작성(아래)
    Product findByProductId(Integer productId);
}
