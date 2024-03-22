package com.goldensnitch.qudditch.repository;

import com.goldensnitch.qudditch.dto.UserStore;
import org.apache.ibatis.annotations.Mapper;
// public interface UserStoreRepository extends JpaRepository<UserStore, Integer>{
//     //UserStore에 대한 추가적인 메서드가 필요하다면 여기에 정의합니다.
//     // 예를 들어, 이메일로 UserStore 찾기:
//     UserStore findByEmail(String email);
// }
@Mapper
public interface UserStoreRepository {
    //UserStore에 대한 추가적인 메서드가 필요하다면 여기에 정의합니다.
    // 이메일로 UserStore 찾기:
    UserStore findByEmail(String email);

    // UserStore를 데이터베이스에 삽입
    void insertUserStore(UserStore userStore);
}
