package com.goldensnitch.qudditch.mapper;

import com.goldensnitch.qudditch.dto.StoreStock;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface StoreStockMapper {
    int cntAllProductByUserStoreId(int userStoreId);
    List<StoreStock> selectAllProductByUserStoreId(int userStoreId);
}
