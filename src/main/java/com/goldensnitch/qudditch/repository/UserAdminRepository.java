package com.goldensnitch.qudditch.repository;

import org.apache.ibatis.annotations.Mapper;

import com.goldensnitch.qudditch.dto.UserAdmin;

@Mapper
public interface UserAdminRepository {

    UserAdmin findByEmail(String email);

    void insertUserAdmin(UserAdmin userAdmin);

    void updateUserAdmin(UserAdmin userAdmin);

    void deleteUserAdminById(Integer id);

    // 필요에 따라 추가적인 메서드 정의
}
