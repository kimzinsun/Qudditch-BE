package com.goldensnitch.qudditch.controller;

import com.goldensnitch.qudditch.dto.CustomerBookmarkStore;
import com.goldensnitch.qudditch.service.ExtendedUserDetails;
import com.goldensnitch.qudditch.service.StoreBookMarkService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("api/store/bookmark")
public class StoreBookMarkController {
    private final StoreBookMarkService storeBookMarkService;

    @Autowired
    public StoreBookMarkController(StoreBookMarkService storeBookMarkService) {
        this.storeBookMarkService = storeBookMarkService;
    }

    @PostMapping("/update/{storeId}")
    public String toggleStoreBookmark(@AuthenticationPrincipal ExtendedUserDetails userDetails, @PathVariable Integer storeId) {
        Integer userCustomerId = userDetails.getId();
        return storeBookMarkService.updateStoreBookmark(userCustomerId, storeId);
    }

    @GetMapping("")
    public List<CustomerBookmarkStore> getStoreBookmarks(@AuthenticationPrincipal ExtendedUserDetails userDetails) {
        int userCustomerId = userDetails.getId();
        return storeBookMarkService.bookmarkList(userCustomerId);
    }




}
