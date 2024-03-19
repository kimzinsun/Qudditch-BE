package com.goldensnitch.qudditch.mapper;

import org.apache.ibatis.annotations.Mapper;

import com.goldensnitch.qudditch.model.UserCustomer;

@Mapper
public interface UserCustomerMapper {
    UserCustomer selectUserByEmail(String email);
    void insertUserCustomer(UserCustomer user);
    void updateUserCustomer(UserCustomer user);
    void deleteUserById(Integer id);
    UserCustomer findByVerificationCode(String verificationCode);
}
