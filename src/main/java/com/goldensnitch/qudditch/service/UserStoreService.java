package com.goldensnitch.qudditch.service;

import com.goldensnitch.qudditch.dto.PaginationParam;
import com.goldensnitch.qudditch.dto.Store;
import com.goldensnitch.qudditch.mapper.UserStoreMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserStoreService {

    private final UserStoreMapper userStoreMapper;

    @Autowired
    public UserStoreService(UserStoreMapper userStoreMapper) {
        this.userStoreMapper = userStoreMapper;
    }

    // 사용자가 선택한 가게의 store_id를 받아 유효성 확인 후 user_store_id 반환
    public Integer selectUserStore(Integer storeId) {
        Integer userStoreId = findUserStoreIdByStoreId(storeId);
        if (userStoreId != null && validateUserStoreId(userStoreId)) {
            return userStoreId;
        }
        else{
            return null;
        }
    }

    // store_id에 대응하는 user_store_id를 조회
    public Integer findUserStoreIdByStoreId(Integer storeId) {
        return userStoreMapper.findUserStoreIdByStoreId(storeId);
    }

    // 주어진 user_store_id가 유효한지 확인
    public boolean validateUserStoreId(Integer userStoreId){
        Integer count = userStoreMapper.countUserStoreById(userStoreId); // 아이디 개수 반환
        return count != null && count > 0;
    }

    public List<Store> searchByStoreName(String name, PaginationParam paginationParam){
        return userStoreMapper.searchByStoreName(name, paginationParam.getRecordSize(), paginationParam.getOffset());
    }

    public int countByStoreName(String name) {
        return userStoreMapper.countByStoreName(name);
    }
}
