package com.goldensnitch.qudditch.service;

import com.goldensnitch.qudditch.mapper.StoreBookMarkMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class StoreBookMarkService {
    private final StoreBookMarkMapper storeBookmarkMapper;

    @Autowired
    public StoreBookMarkService(StoreBookMarkMapper storeBookmarkMapper) {
        this.storeBookmarkMapper = storeBookmarkMapper;
    }

    public void addStoreBookmark(Integer userCustomerId, Integer userStoreId) {
        Map<String, Integer> params = new HashMap<>();
        params.put("userCustomerId", userCustomerId);
        params.put("userStoreId", userStoreId);
        storeBookmarkMapper.addStoreBookmark(params);
    }

    public void removeStoreBookmark(Integer userCustomerId, Integer userStoreId) {
        Map<String, Integer> params = new HashMap<>();
        params.put("userCustomerId", userCustomerId);
        params.put("userStoreId", userStoreId);
        storeBookmarkMapper.removeStoreBookmark(params);
    }
}
