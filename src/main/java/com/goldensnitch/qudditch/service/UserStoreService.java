package com.goldensnitch.qudditch.service;

import com.goldensnitch.qudditch.mapper.UserStoreMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserStoreService {

    @Autowired
    private UserStoreMapper userStoreMapper;

    // store_id에 대응하는 user_store_id를 조회
    public Integer findUserStoreIdByStoreId(Integer storeId) {
        return userStoreMapper.findUserStoreIdByStoreId(storeId);
    }

    // 주어진 user_store_id가 유효한지 확인
    public boolean validateUserStoreId(Integer userStoreId){
        Integer count = userStoreMapper.countUserStoreById(userStoreId);
        return count != null && count > 0;
    }

}
