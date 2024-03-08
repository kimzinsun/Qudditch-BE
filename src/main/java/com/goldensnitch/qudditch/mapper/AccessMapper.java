package com.goldensnitch.qudditch.mapper;

import com.goldensnitch.qudditch.dto.StoreVisitorLog;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface AccessMapper {
    void insertVisitLog(StoreVisitorLog storeVisitorLog);
}
