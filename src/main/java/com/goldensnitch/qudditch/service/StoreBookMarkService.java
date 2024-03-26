package com.goldensnitch.qudditch.service;

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

    public String toggleStoreBookmark(Integer userCustomerId, Integer storeId) {
       int count = storeBookmarkMapper.toggleStoreBookmark(userCustomerId, storeId);
        if (count == 0){
            storeBookmarkMapper.addStoreBookmark(userCustomerId, storeId);
            return "관심 스토어 등록 성공 !";
        }else {
            storeBookmarkMapper.deleteStoreBookmark(userCustomerId, storeId);
            return "관심 스토어 설정 해제 !";
        }
    }




}
