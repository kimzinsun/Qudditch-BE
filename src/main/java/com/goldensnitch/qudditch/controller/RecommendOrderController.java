package com.goldensnitch.qudditch.controller;

import com.goldensnitch.qudditch.dto.Pagination;
import com.goldensnitch.qudditch.dto.PaginationParam;
import com.goldensnitch.qudditch.dto.RecommendOrder.RecommendOrderReq;
import com.goldensnitch.qudditch.service.RecommendOrderService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/api/store/recommend")
public class RecommendOrderController {

    private final RecommendOrderService recommendOrderService;
    @Autowired
    public RecommendOrderController(RecommendOrderService recommendOrderService) {
        this.recommendOrderService = recommendOrderService;
    }
    @GetMapping("")
    public Map<String, Object> selectProductByQty(@RequestParam int userStoreId, PaginationParam paginationParam){

        int count = recommendOrderService.cntSelectProductByQty(userStoreId);

        List<RecommendOrderReq> list = recommendOrderService.selectProductByQty(userStoreId, paginationParam);
        Map<String, Object> map = new HashMap<>();
        map.put("list", list);

        Pagination pagination = new Pagination(count, paginationParam);
        map.put("pagination", pagination);

        return map;
    }

}
