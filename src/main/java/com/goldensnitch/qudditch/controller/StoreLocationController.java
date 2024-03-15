package com.goldensnitch.qudditch.controller;

import com.goldensnitch.qudditch.dto.Store;
import com.goldensnitch.qudditch.dto.StoreStockRes;
import com.goldensnitch.qudditch.service.StoreLocationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/store/location")
public class StoreLocationController {
    private final StoreLocationService storeLocationService;
    @Autowired
    public StoreLocationController(StoreLocationService storeLocationService) {
        this.storeLocationService = storeLocationService;
    }

    // 현재위치 값을 넣을 시 근처 스토어
    @GetMapping("")
    public List<Store> getLocation(@RequestParam double currentWgs84X, double currentWgs84Y){

        return storeLocationService.getLocation(currentWgs84X, currentWgs84Y);
    }

    // storeId를 받고 난 userStoreId들의 재고 리스트
    @GetMapping("/stock")
    public List<StoreStockRes> storeStockList(@RequestParam int userStoreId){
       int storeId = storeLocationService.getUserstoreIdBystoreId(userStoreId);

       return storeLocationService.storeStockList(storeId);
    }






}
