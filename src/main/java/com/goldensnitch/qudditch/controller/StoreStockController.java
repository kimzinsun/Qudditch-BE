package com.goldensnitch.qudditch.controller;

import com.goldensnitch.qudditch.dto.*;
import com.goldensnitch.qudditch.service.RedisService;
import com.goldensnitch.qudditch.service.StoreStockService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@Slf4j
@RequestMapping("/api/store")
public class StoreStockController {
    private final StoreStockService storeStockService;
    private final RedisService redisService;


    public StoreStockController(StoreStockService storeStockService, RedisService redisService) {
        this.storeStockService = storeStockService;
        this.redisService = redisService;
    }
    // TODO : store 관련 기능 구현

    @GetMapping("/stock")
    public Map<String, Object> getStockList(@RequestParam @Nullable Integer categoryId) {
//        int userStoreId = (int) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        int userStoreId = 2;
        int count = categoryId == null ? storeStockService.cntProductByUserStoreId(userStoreId) : storeStockService.cntProductByUserStoreIdAndCategoryId(userStoreId, categoryId);
        List<StoreStockRes> stockList = categoryId == null ? storeStockService.selectAllProductByUserStoreId(userStoreId) : storeStockService.selectProductByUserStoreIdAndCategoryId(userStoreId, categoryId);
        Map<String, Object> map = new HashMap<String, Object>();
        int page = count / 10;
        if (count % 10 > 0) {
            page += 1;
        }
        map.put("stockList", stockList);
        map.put("count", count);
        map.put("page", page);

        return map;

    }


    @PostMapping("/stock/update")
    public String updateStock(@RequestBody List<StockUpdateReq> stockUpdateReq) {
        int userStoreId = (int) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
//        int userStoreId = 2;
        for (StockUpdateReq req : stockUpdateReq) {
            StoreStock storeStock = storeStockService.selectProductByUserStoreIdAndProductId(userStoreId, req.getProductId());
            if (req.getQuantity() != null) {
                storeStock.setQty(req.getQuantity());
            }
            if (req.getPositionId() != null) {
                storeStock.setPositionId(req.getPositionId());
            }
            storeStockService.updateStock(storeStock);
        }
        return "success";
    }

    @GetMapping("/stock/{productId}")
    public List<StoreLocQty> getLocation(@PathVariable int productId, @RequestParam double currentWgs84X, double currentWgs84Y) {
        return storeStockService.getStoreByProductId(productId, currentWgs84X, currentWgs84Y);
    }

    @PostMapping("/stock/dispose")
    public String disposeProduct(@RequestBody List<DisposeReq> list) {
//        int userStoreId = (int) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        int userStoreId = 2;
        for (DisposeReq req : list) {
            StoreStock storeStock = storeStockService.selectProductByUserStoreIdAndProductId(userStoreId, req.getProductId());
            storeStock.setQty(storeStock.getQty() - req.getQty());
            storeStockService.updateStock(storeStock);
            storeStockService.insertDisposeLog(userStoreId,req.getProductId(),req.getQty());
        }
        return "success";
    }

    @GetMapping("/stock/dispose")
    public Map<String, Object> getDisposeLog() {
//        int userStoreId = (int) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        int userStoreId = 2;

        Map<String, Object> map = new HashMap<>();
        int count = storeStockService.getDisposeLogCount(userStoreId);
        List<DisposeLog> disposeLog = storeStockService.getDisposeLog(userStoreId);
        int page = count / 10;
        if (count % 10 > 0) {
            page += 1;
        }
        map.put("disposeLog", disposeLog);
        map.put("count", count);
        map.put("page", page);
        return map;
    }

}
