package com.goldensnitch.qudditch.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.goldensnitch.qudditch.model.UserStore;

public interface UserStoreRepository extends JpaRepository<UserStore, Integer>{
    //UserStore에 대한 추가적인 메서드가 필요하다면 여기에 정의합니다.
    // 예를 들어, 이메일로 UserStore 찾기:
    UserStore findByEmail(String email);
}
