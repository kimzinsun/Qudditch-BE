package com.goldensnitch.qudditch.mapper;

import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface StoreBookMarkMapper {

    int toggleStoreBookmark(Integer userCustomerId, Integer storeId);

    int addStoreBookmark(Integer userCustomerId,Integer storeId);

    int deleteStoreBookmark(Integer userCustomerId,Integer storeId);





}
