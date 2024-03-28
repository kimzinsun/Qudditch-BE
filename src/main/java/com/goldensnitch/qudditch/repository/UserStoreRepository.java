package com.goldensnitch.qudditch.repository;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;

import com.goldensnitch.qudditch.dto.UserStore;
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
    @Insert("INSERT INTO user_store (email, password, name, bn_number, state) VALUES (#{email}, #{password}, #{name}, #{bnNumber}, #{state})")
    @Options(useGeneratedKeys=true, keyProperty="id")
    void insertUserStore(UserStore userStore);


}
