package com.goldensnitch.qudditch.mapper;

import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface VisitorMapper {

    Integer getDailyVisitor(int storeId, String date);

    Integer getMonthVisitor(int storeId, String yearMonth);
}
