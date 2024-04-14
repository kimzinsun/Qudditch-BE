package com.goldensnitch.qudditch.controller;

import com.goldensnitch.qudditch.dto.Pagination;
import com.goldensnitch.qudditch.dto.PaginationParam;
import com.goldensnitch.qudditch.dto.RecommendOrder.RecommendOrderReq;
import com.goldensnitch.qudditch.dto.StoreStockRes;
import com.goldensnitch.qudditch.service.ExtendedUserDetails;
import com.goldensnitch.qudditch.service.RecommendOrderService;
import com.goldensnitch.qudditch.service.RedisService;
import com.goldensnitch.qudditch.service.StoreStockService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/api/store/recommend")
public class RecommendOrderController {

    private final RecommendOrderService recommendOrderService;
    private final RedisService redisService;
    @Autowired
    public RecommendOrderController(RecommendOrderService recommendOrderService, RedisService redisService, StoreStockService storeStockService) {
        this.recommendOrderService = recommendOrderService;
        this.redisService = redisService;
    }
    @GetMapping("")
    public Map<String, Object> selectProductByQty(@AuthenticationPrincipal ExtendedUserDetails userDetails, PaginationParam paginationParam){

        int userStoreId = userDetails.getId();

        int count = recommendOrderService.cntSelectProductByQty(userStoreId);

        List<RecommendOrderReq> list = recommendOrderService.selectProductByQty(userStoreId, paginationParam);
        Map<String, Object> map = new HashMap<>();
        map.put("list", list);

        Pagination pagination = new Pagination(count, paginationParam);
        map.put("pagination", pagination);

        return map;
    }

    @GetMapping("/emotion")
    public ResponseEntity<Map<String, Object>> selectProductByEmotion(@AuthenticationPrincipal ExtendedUserDetails userDetails) {
        Map<String, Object> response = new HashMap<>();
        int userId = userDetails.getId();
        if(redisService.checkExistsKey("emotion-"+userId)){
            String emotion = (String) recommendOrderService.selectProductByEmotion(redisService.getHashOps("emotion-"+userId, "emotion")).get("emotion");
            Map<String, Object> data = recommendOrderService.selectProductByEmotion(redisService.getHashOps("emotion-"+userId, "emotion"));

            if(data.get("status") == "success"){
                response.put("status", "success");
                response.put("emotion", emotion);
                response.put("data", data.get("data"));
            }
            else{
                response.put("status", "fail");
                response.put("message", "제품이 존재하지 않습니다");
            }

        }
        else{
            response.put("status", "fail");
            response.put("message", "오류가 발생했습니다");
        }
        return ResponseEntity.ok(response);

    }

}
