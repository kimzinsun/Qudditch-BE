package com.goldensnitch.qudditch.mapper;

import com.goldensnitch.qudditch.dto.Product;
import com.goldensnitch.qudditch.dto.StoreStock;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface ProductMapper {
    void insertProduct(Integer categoryId, String brand, String name, String description, String image, Integer price, String unitPrice, Integer expirationDate);
    Product selectProductById(Integer id);

    List<Product> selectProductByName(String productName);

    List<StoreStock> selectStoreStockByProductId(Integer productId, double currentWgs84X, double currentWgs84Y, int recordSize, int offset);

    int cntStoreStockByProductId(Integer productId);

    int selectProductIdByName(String productName);
}
