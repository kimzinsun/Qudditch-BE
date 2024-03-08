package com.goldensnitch.qudditch.controller;

import com.goldensnitch.qudditch.dto.StoreStock;
import com.goldensnitch.qudditch.dto.StoreStockRes;
import com.goldensnitch.qudditch.service.StoreStockService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@Slf4j
@RequestMapping("/api/store")
public class StoreStockController {
    final StoreStockService storeStockService;

    public StoreStockController(StoreStockService storeStockService) {
        this.storeStockService = storeStockService;
    }
    // TODO : store 관련 기능 구현

    @GetMapping("/stock")
    public List<StoreStockRes> getStockList() {
        int userStoreId = 2;
//        int userStoreId = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return storeStockService.selectAllProductByUserStoreId(userStoreId);
    }


}
