package com.goldensnitch.qudditch.mapper;

import com.goldensnitch.qudditch.dto.UserPersona;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface PersonaMapper {
    void insertPersona(UserPersona userPersona);
}
