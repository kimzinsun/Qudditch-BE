package com.goldensnitch.qudditch.controller;

import com.goldensnitch.qudditch.dto.Pagination;
import com.goldensnitch.qudditch.dto.PaginationParam;
import com.goldensnitch.qudditch.dto.Store;
import com.goldensnitch.qudditch.service.UserStoreService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/userstore")
public class UserStoreController {

    private final UserStoreService userStoreService;

    @Autowired
    public UserStoreController(UserStoreService userStoreService) {
        this.userStoreService = userStoreService;
    }

    @GetMapping("")
    public ResponseEntity<?> selectStore(@RequestParam Integer storeId){
        Integer userStoreId = userStoreService.selectUserStore(storeId);
        if(userStoreId != null) {
            return ResponseEntity.ok().body("{\"userStoreId\": \"" + userStoreId + "\"}");
        } else {
            return ResponseEntity.ok().body("{\"userStoreId\": null}");
        }
    }

    @GetMapping("/search")
    public ResponseEntity<Map<String, Object>> searchByStoreName(@RequestParam String name, PaginationParam paginationParam){
        try{
            int count = userStoreService.countByStoreName(name);
            List<Store> stores = userStoreService.searchByStoreName(name, paginationParam);
            return ResponseEntity.ok(Map.of("status","success","data", stores, "pagination", new Pagination(count, paginationParam)));
        } catch (Exception e) {
            return ResponseEntity.ok(Map.of("status", "fail", "message", e.getMessage()));

        }
    }
}
