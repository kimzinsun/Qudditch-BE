package com.goldensnitch.qudditch.mapper;

import com.goldensnitch.qudditch.dto.graph.CategorySalesDto;
import com.goldensnitch.qudditch.dto.graph.DailyHourVisitorDto;
import com.goldensnitch.qudditch.dto.graph.DailySalesDto;
import com.goldensnitch.qudditch.dto.graph.DailyVisitorDto;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface GraphMapper {
    List<DailySalesDto> selectSalesList(int userStoreId, String yearMonth);
    List<CategorySalesDto> selectCategoryList(int userStoreId, String yearMonth);
    List<DailyVisitorDto> selectDailyVisitorList(int userStoreId, String yearMonth);
    List<DailyHourVisitorDto> selectDailyHourVisitorList(int userStoreId, String date);
}
