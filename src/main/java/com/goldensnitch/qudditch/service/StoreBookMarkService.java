package com.goldensnitch.qudditch.service;

import com.goldensnitch.qudditch.dto.CustomerBookmarkStore;
import com.goldensnitch.qudditch.mapper.StoreBookMarkMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class StoreBookMarkService {
    private final StoreBookMarkMapper storeBookmarkMapper;

    @Autowired
    public StoreBookMarkService(StoreBookMarkMapper storeBookmarkMapper) {
        this.storeBookmarkMapper = storeBookmarkMapper;
    }

    public void storeBookmark(CustomerBookmarkStore customerBookmarkStore) {
        storeBookmarkMapper.storeBookmark(customerBookmarkStore);
    }
}
