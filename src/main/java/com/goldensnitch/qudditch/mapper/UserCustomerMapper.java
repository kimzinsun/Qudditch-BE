package com.goldensnitch.qudditch.mapper;

import org.apache.ibatis.annotations.Mapper;

import com.goldensnitch.qudditch.model.UserCustomer;

@Mapper
public interface UserCustomerMapper {
    UserCustomer selectUserByEmail(String email);
    Integer insertUserCustomer(UserCustomer user);
}
