package com.goldensnitch.qudditch.mapper;

import org.apache.ibatis.annotations.Mapper;

import com.goldensnitch.qudditch.dto.SocialLogin;

@Mapper
public interface SocialLoginMapper {
    // XML 파일에 정의된 SQL 매핑을 사용하도록 변경
    SocialLogin selectByToken(String token);
    int updateSocialLogin(SocialLogin socialLogin);
    void insertSocialLogin(com.goldensnitch.qudditch.service.SocialLogin socialLogin);
}