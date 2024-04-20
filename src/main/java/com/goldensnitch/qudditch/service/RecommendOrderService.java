package com.goldensnitch.qudditch.service;

import com.goldensnitch.qudditch.dto.PaginationParam;
import com.goldensnitch.qudditch.dto.RecommendOrder.RecommendOrderReq;
import com.goldensnitch.qudditch.dto.StoreStockRes;
import com.goldensnitch.qudditch.mapper.RecommendOrderMapper;
import com.goldensnitch.qudditch.mapper.StoreStockMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

import static java.lang.Integer.parseInt;

@Service
public class RecommendOrderService {

    private final RecommendOrderMapper recommendOrderMapper;

    public RecommendOrderService(RecommendOrderMapper recommendOrderMapper, StoreStockMapper storeStockMapper) {
        this.recommendOrderMapper = recommendOrderMapper;
    }

    public List<RecommendOrderReq> selectProductByQty(int userStoreId, PaginationParam paginationParam) {
        return recommendOrderMapper.selectProductByQty(userStoreId, paginationParam.getRecordSize(), paginationParam.getOffset());
    }

    public int cntSelectProductByQty(int userStoreId) {
        return recommendOrderMapper.cntSelectProductByQty(userStoreId);
    }


    public Map<String, Object> selectProductByEmotion(Map<Object, Object> emotion) {
        String emotions = emotion.get("emotion").toString();
        int storeId = parseInt(emotion.get("storeId").toString());

        List<StoreStockRes> storeStocks = switch (emotions) {
            case "CALM", "HAPPY" -> recommendOrderMapper.selectProductByUserStoreIdAndCategoryId(storeId, 1);
            case "SAD", "ANGRY" -> recommendOrderMapper.selectProductByUserStoreIdAndCategoryId(storeId, 11);
            case "SURPRISED" -> recommendOrderMapper.selectProductByUserStoreIdAndCategoryId(storeId, 2);
            default -> List.of();
        };

        if (storeStocks.isEmpty()) {
            return Map.of("status", "fail");
        } else {
            int random = (int) (Math.random() * storeStocks.size());
            return Map.of("status", "success", "emotion", emotions, "data", storeStocks.get(random));
        }


    }

    public List<RecommendOrderReq> selectBestProduct(int storeId) {
        return recommendOrderMapper.selectBestProduct(storeId);
    }
}
