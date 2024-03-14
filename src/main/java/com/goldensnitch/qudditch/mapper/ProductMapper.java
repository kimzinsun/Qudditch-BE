package com.goldensnitch.qudditch.mapper;

import com.goldensnitch.qudditch.dto.Product;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface ProductMapper {
    void insertProduct(Integer categoryId, String brand, String name, String description, String image, Integer price, String unitPrice, Integer expirationDate);

    // 제품 조회
    Product findByProductId(Integer productId);
    Product selectProductById(Integer id);
}
