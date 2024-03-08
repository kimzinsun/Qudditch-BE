package com.goldensnitch.qudditch.mapper;

import com.goldensnitch.qudditch.dto.graph.DailySalesDto;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface GraphMapper {
    List<DailySalesDto> selectSalesList(Integer userStoreId, String yearMonth);
}
