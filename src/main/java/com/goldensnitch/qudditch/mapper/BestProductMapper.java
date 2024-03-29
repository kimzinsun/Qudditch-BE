package com.goldensnitch.qudditch.mapper;

import com.goldensnitch.qudditch.dto.StoreStockReport;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface BestProductMapper {
    List<StoreStockReport> BestProduct(Integer storeId);
}
