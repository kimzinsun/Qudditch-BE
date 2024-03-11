package com.goldensnitch.qudditch.service;

import com.goldensnitch.qudditch.dto.Store;
import com.goldensnitch.qudditch.dto.StoreLocQty;
import com.goldensnitch.qudditch.dto.StoreStock;
import com.goldensnitch.qudditch.dto.StoreStockRes;
import com.goldensnitch.qudditch.mapper.StoreStockMapper;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class StoreStockService {
    final StoreStockMapper storeStockMapper;

    public StoreStockService(StoreStockMapper storeStockMapper) {
        this.storeStockMapper = storeStockMapper;
    }

    public List<StoreStockRes> selectAllProductByUserStoreId(int userStoreId) {
        return storeStockMapper.selectAllProductByUserStoreId(userStoreId);
    }

    public void updateStock(StoreStock storeStock) {
        storeStockMapper.updateStock(storeStock);
    }

    public StoreStock selectProductByUserStoreIdAndProductId(int userStoreId, int productId) {
        return storeStockMapper.selectProductByUserStoreIdAndProductId(userStoreId, productId);
    }

    public List<StoreStockRes> selectProductByUserStoreIdAndCategoryId(int userStoreId, Integer categoryId) {
        return storeStockMapper.selectProductByUserStoreIdAndCategoryId(userStoreId, categoryId);
    }

    public List<StoreLocQty> getStoreByProductId(Integer productId, double currentWgs84X, double currentWgs84Y) {
        return storeStockMapper.selectStoreByProductId(productId, currentWgs84X, currentWgs84Y);
    }

}
