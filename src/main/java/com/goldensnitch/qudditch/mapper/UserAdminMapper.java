package com.goldensnitch.qudditch.mapper;

import com.goldensnitch.qudditch.dto.UserAdmin;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserAdminMapper {

    UserAdmin findByEmail(String email);

    void insertUserAdmin(UserAdmin userAdmin);

    void updateUserAdmin(UserAdmin userAdmin);

    void deleteUserAdminById(Integer id);

    // 필요에 따라 추가적인 메서드 정의
}
