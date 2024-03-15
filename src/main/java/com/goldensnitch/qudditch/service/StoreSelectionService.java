package com.goldensnitch.qudditch.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class StoreSelectionService {

    @Autowired
    private UserStoreService userStoreService;

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
