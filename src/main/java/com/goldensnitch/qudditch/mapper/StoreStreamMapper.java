package com.goldensnitch.qudditch.mapper;

import com.goldensnitch.qudditch.dto.StoreStream;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface StoreStreamMapper {
    Integer insertStoreStream(StoreStream storeStream);

    StoreStream selectStoreStreamByUserStoreId(Integer userStoreId);

    Integer deleteStoreStreamByUserStoreId(Integer userStoreId);
}
