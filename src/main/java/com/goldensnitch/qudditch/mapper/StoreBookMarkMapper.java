package com.goldensnitch.qudditch.mapper;

import com.goldensnitch.qudditch.dto.CustomerBookmarkStore;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface StoreBookMarkMapper {

    int toggleStoreBookmark(Integer userCustomerId, Integer storeId);

    int addStoreBookmark(Integer userCustomerId,Integer storeId);

    int deleteStoreBookmark(Integer userCustomerId,Integer storeId);

    List<CustomerBookmarkStore> bookmarkList(Integer userCustomerId);





}
