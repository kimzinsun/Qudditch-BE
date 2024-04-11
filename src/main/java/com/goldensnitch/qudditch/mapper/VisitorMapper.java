package com.goldensnitch.qudditch.mapper;

import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface VisitorMapper {

    Integer getDailyVisitor(Integer storeId, String date);

    Integer getMonthVisitor(Integer storeId, String yearMonth);
}
