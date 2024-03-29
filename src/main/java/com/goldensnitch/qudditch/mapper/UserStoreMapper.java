package com.goldensnitch.qudditch.mapper;

import org.apache.ibatis.annotations.Mapper;

import com.goldensnitch.qudditch.dto.UserStore;

@Mapper
public interface UserStoreMapper {

    // 이메일로 UserStore 찾기:
    UserStore findByEmail(String email);

    // 현재 최대 store_id 값을 찾는 메서드
    Integer findMaxStoreId();

    // UserStore를 데이터베이스에 삽입
    void insertUserStore(UserStore userStore);

    // store_id에 대응하는 user_store_id를 조회
    Integer findUserStoreIdByStoreId(Integer storeId);

    // 특정 매장 서비스 사용 가능 여부
    Integer countUserStoreById(Integer userStoreId);
}
