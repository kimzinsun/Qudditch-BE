package com.goldensnitch.qudditch.mapper;

import org.apache.ibatis.annotations.MapKey;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface VisitorMapper {

    Integer getDailyVisitor(Integer storeId, String date);

    Integer getMonthVisitor(Integer storeId, String yearMonth);

    @MapKey("age")
    List<Map<String, Object>> getAgeGenderVisitor();

    @MapKey("gender")
    List<Map<String, Object>> getGenderVisitor();
}
