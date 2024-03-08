package com.goldensnitch.qudditch.mapper;

import com.goldensnitch.qudditch.dto.graph.MonthlySalesDto;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface GraphMapper {
    int selectTest();
    List<MonthlySalesDto> selectSalesList(Integer userStoreId);
}
