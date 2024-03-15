package com.goldensnitch.qudditch.controller;

import com.goldensnitch.qudditch.dto.StoreBookmark.BookmarkReq;
import com.goldensnitch.qudditch.service.StoreBookMarkService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("api/store/bookmark")
public class StoreBookMarkController {
    private final StoreBookMarkService storeBookMarkService;

    @Autowired
    public StoreBookMarkController(StoreBookMarkService storeBookMarkService) {
        this.storeBookMarkService = storeBookMarkService;
    }

    // 0이면 insert , 1 이면 delete
    @PostMapping("/toggle")
    public boolean toggleStoreBookmark(@RequestBody BookmarkReq request) {
        return storeBookMarkService.toggleStoreBookmark(request.getUserCustomerId(), request.getStoreId());
    }





}
