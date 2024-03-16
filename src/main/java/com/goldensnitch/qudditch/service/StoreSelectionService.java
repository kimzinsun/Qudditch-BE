package com.goldensnitch.qudditch.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class StoreSelectionService {

    @Autowired
    private UserStoreService userStoreService;

    // 사용자가 선택한 가게의 store_id를 받아 유효성 확인 후 user_store_id 반환
    public Integer selectServiceStore(Integer storeId) {
        Integer userStoreId = userStoreService.findUserStoreIdByStoreId(storeId);
        if (userStoreId != null && userStoreService.validateUserStoreId(userStoreId)) {
            return userStoreId;
        }
        else{
            return null;
        }
    }
}
