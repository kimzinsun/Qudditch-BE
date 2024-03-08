package com.goldensnitch.qudditch.service;

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

}
