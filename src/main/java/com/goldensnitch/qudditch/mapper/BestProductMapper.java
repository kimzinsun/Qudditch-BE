package com.goldensnitch.qudditch.mapper;

import com.goldensnitch.qudditch.dto.UserStore;
import org.apache.ibatis.annotations.MapKey;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface BestProductMapper {
    List<UserStore> BestProduct(Integer Id);

    @MapKey("age")
    List<Map<String, Object>> bestProductByAge();

    @MapKey("gender")
    List<Map<String, Object>> bestProductByGender();
}
