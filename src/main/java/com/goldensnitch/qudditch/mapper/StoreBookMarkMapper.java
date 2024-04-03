package com.goldensnitch.qudditch.mapper;

import com.goldensnitch.qudditch.dto.CustomerBookmarkStore;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface StoreBookMarkMapper {

    int addStoreBookmark(Integer userCustomerId,Integer storeId);

    int deleteAllStoreBookmark(Integer userCustomerId);

    List<CustomerBookmarkStore> bookmarkList(Integer userCustomerId);





}
