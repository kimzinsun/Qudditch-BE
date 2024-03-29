package com.goldensnitch.qudditch.controller;

import com.goldensnitch.qudditch.dto.CustomerBookmarkStore;
import com.goldensnitch.qudditch.service.ExtendedUserDetails;
import com.goldensnitch.qudditch.service.StoreBookMarkService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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

    @PostMapping("/toggle")
    public String toggleStoreBookmark(@AuthenticationPrincipal ExtendedUserDetails userDetails, int storeId) {
        int userCuseromerId = userDetails.getId();

        return storeBookMarkService.toggleStoreBookmark(userCuseromerId, storeId);
    }

    @GetMapping("")
    public List<CustomerBookmarkStore> getStoreBookmarks(@AuthenticationPrincipal ExtendedUserDetails userDetails) {
        int userCustomerId = userDetails.getId();
        return storeBookMarkService.bookmarkList(userCustomerId);
    }




}
