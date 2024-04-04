package com.goldensnitch.qudditch.controller;

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
            return ResponseEntity.ok().body("userStore ID: " + userStoreId);
        } else {
            return ResponseEntity.badRequest().body("Invalid store ID or store could not be selected.");
        }
    }

    @GetMapping("/search")
    public ResponseEntity<?> searchByStoreName(@RequestParam String name){
        try{
            List<Store> stores = userStoreService.searchByStoreName(name);
            return ResponseEntity.ok(stores);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("매장 검색 중 오류가 발생했습니다.");

        }
    }
}
