package com.goldensnitch.qudditch.mapper;

import com.goldensnitch.qudditch.dto.*;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface StoreStockMapper {
    int cntProductByUserStoreId(int userStoreId);
    List<StoreStockRes> selectAllProductByUserStoreId(int userStoreId);
    StoreStock selectProductByUserStoreIdAndProductId(int userStoreId, int productId);

    void updateStock(StoreStock storeStock);

    List<StoreStockRes> selectProductByUserStoreIdAndCategoryId(int userStoreId, Integer categoryId);

    List<StoreLocQty> selectStoreByProductId(Integer productId, double currentWgs84X, double currentWgs84Y);

    int cntProductByUserStoreIdAndCategoryId(int userStoreId, Integer categoryId);

    void insertDisposeLog(int userStoreId, int productId, int qty);

    int getDisposeLogCount(int userStoreId);

    List<DisposeLog> getDisposeLog(int userStoreId);
}
