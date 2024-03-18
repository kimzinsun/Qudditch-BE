package com.goldensnitch.qudditch.service;

import com.goldensnitch.qudditch.dto.PaginationParam;
import com.goldensnitch.qudditch.dto.RecommendOrder.RecommendOrderReq;
import com.goldensnitch.qudditch.mapper.RecommendOrderMapper;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RecommendOrderService {

    private final RecommendOrderMapper recommendOrderMapper;

    public RecommendOrderService(RecommendOrderMapper recommendOrderMapper) {
        this.recommendOrderMapper = recommendOrderMapper;
    }

    public List<RecommendOrderReq> selectProductByQty(int userStoreId, PaginationParam paginationParam) {
        return recommendOrderMapper.selectProductByQty(userStoreId,paginationParam.getRecordSize(), paginationParam.getOffset());
    }

    public int cntSelectProductByQty(int userStoreId) {
        return recommendOrderMapper.cntSelectProductByQty(userStoreId);
    }



}
