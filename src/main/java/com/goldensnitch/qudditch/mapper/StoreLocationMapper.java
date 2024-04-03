package com.goldensnitch.qudditch.mapper;

import com.goldensnitch.qudditch.dto.Store;
import com.goldensnitch.qudditch.dto.StoreStockRes;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface StoreLocationMapper {

    List<Store> getLocation(Map<String, Object> params);

    int getUserstoreIdBystoreId(int storeId);

    List<StoreStockRes> storeStockList(int userStoreId, int recordSize, int offset);

    int cntStoreStockList(int userStoreId);



}
