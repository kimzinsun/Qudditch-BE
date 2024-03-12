package com.goldensnitch.qudditch.mapper;

import com.goldensnitch.qudditch.dto.CustomerBookmarkStore;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface StoreBookMarkMapper {
    void storeBookmark(CustomerBookmarkStore customerBookmarkStore);
}
