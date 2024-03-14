package com.goldensnitch.qudditch.mapper;

import com.goldensnitch.qudditch.dto.StoreLocQty;
import com.goldensnitch.qudditch.dto.StoreStock;
import com.goldensnitch.qudditch.dto.StoreStockReport;
import com.goldensnitch.qudditch.dto.StoreStockRes;
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

    void updateStockQty(Integer userStoreId, Integer productId, Integer newQty);

    void insertStoreStockReport(StoreStockReport stockReport);
}
