package com.goldensnitch.qudditch.mapper;

import com.goldensnitch.qudditch.dto.StoreStock;
import com.goldensnitch.qudditch.dto.StoreStockRes;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface StoreStockMapper {
    int cntAllProductByUserStoreId(int userStoreId);
    List<StoreStockRes> selectAllProductByUserStoreId(int userStoreId);
}
