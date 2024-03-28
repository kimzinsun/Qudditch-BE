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
    
}
