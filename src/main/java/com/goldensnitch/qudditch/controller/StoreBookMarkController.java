package com.goldensnitch.qudditch.controller;

import com.goldensnitch.qudditch.service.StoreBookMarkService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("api/store/bookmark")
public class StoreBookMarkController {
    private final StoreBookMarkService storeBookMarkService;

    @Autowired
    public StoreBookMarkController(StoreBookMarkService storeBookMarkService) {
        this.storeBookMarkService = storeBookMarkService;
    }

    @PostMapping("/add")
    public void addStoreBookmark(@RequestParam Integer userCustomerId, @RequestParam Integer userStoreId) {
        storeBookMarkService.addStoreBookmark(userCustomerId, userStoreId);
    }
    @PostMapping("/remove")
    public void removeStoreBookmark(@RequestParam Integer userCustomerId, @RequestParam Integer userStoreId) {
        storeBookMarkService.removeStoreBookmark(userCustomerId, userStoreId);
    }





}
