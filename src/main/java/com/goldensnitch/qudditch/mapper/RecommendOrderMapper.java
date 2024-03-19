package com.goldensnitch.qudditch.mapper;

import com.goldensnitch.qudditch.dto.RecommendOrder.RecommendOrderReq;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface RecommendOrderMapper {

    List<RecommendOrderReq> selectProductByQty(int userStoreId, int recordSize, int offset);

    int cntSelectProductByQty(int userStoreId);

}
