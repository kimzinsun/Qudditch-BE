package com.goldensnitch.qudditch.mapper;

import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserStoreMapper {

    // store_id에 대응하는 user_store_id를 조회
    Integer findUserStoreIdByStoreId(Integer storeId);

    // 특정 매장 서비스 사용 가능 여부
    Integer countUserStoreById(Integer userStoreId);
}
