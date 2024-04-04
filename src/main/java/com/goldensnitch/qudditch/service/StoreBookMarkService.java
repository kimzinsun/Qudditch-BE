package com.goldensnitch.qudditch.service;

import com.goldensnitch.qudditch.dto.CustomerBookmarkStore;
import com.goldensnitch.qudditch.mapper.StoreBookMarkMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class StoreBookMarkService {
    private final StoreBookMarkMapper storeBookmarkMapper;

    @Autowired
    public StoreBookMarkService(StoreBookMarkMapper storeBookmarkMapper) {
        this.storeBookmarkMapper = storeBookmarkMapper;
    }


    public String updateStoreBookmark(Integer userCustomerId, Integer storeId){
        // 먼저 모든 북마크 제거
        storeBookmarkMapper.deleteAllStoreBookmark(userCustomerId);

        // 새 스토어 추가
        storeBookmarkMapper.addStoreBookmark(userCustomerId, storeId);
        return "관심 스토어 등록 성공 !";
    }

    public List<CustomerBookmarkStore> bookmarkList(Integer userCustomerId) {
        return storeBookmarkMapper.bookmarkList(userCustomerId);
    }




}
