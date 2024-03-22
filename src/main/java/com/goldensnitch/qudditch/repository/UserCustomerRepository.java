// File: src/main/java/com/goldensnitch/qudditch/repository/UserCustomerRepository.java

package com.goldensnitch.qudditch.repository;

import org.apache.ibatis.annotations.Mapper;

import com.goldensnitch.qudditch.dto.UserCustomer;
@Mapper // Mybatis 사용 시 @Mapper 어노테이션 사용
public interface UserCustomerRepository {
    UserCustomer selectUserByEmail(String email);
    Integer insertUserCustomer(UserCustomer user);
    UserCustomer findByEmail(String email); // 이메일을 기반으로 사용자를 찾는 메소드 추가
    UserCustomer findByVerificationCode(String verificationCode);
    void updateUserCustomer(UserCustomer userCustomer);
}