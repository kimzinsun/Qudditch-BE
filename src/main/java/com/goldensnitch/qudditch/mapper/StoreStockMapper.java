package com.goldensnitch.qudditch.mapper;

import com.goldensnitch.qudditch.dto.Store;
import com.goldensnitch.qudditch.dto.StoreStock;
import com.goldensnitch.qudditch.dto.StoreStockRes;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface StoreStockMapper {
    int cntAllProductByUserStoreId(int userStoreId);
    List<StoreStockRes> selectAllProductByUserStoreId(int userStoreId);
    StoreStock selectProductByUserStoreIdAndProductId(int userStoreId, int productId);

    void updateStock(StoreStock storeStock);

    List<StoreStockRes> selectProductByUserStoreIdAndCategoryId(int userStoreId, Integer categoryId);

    List<Store> selectStoreByProductId(Integer productId);
}
