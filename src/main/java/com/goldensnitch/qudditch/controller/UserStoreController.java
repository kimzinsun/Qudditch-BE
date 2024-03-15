package com.goldensnitch.qudditch.controller;

import com.goldensnitch.qudditch.service.UserStoreService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/userstore")
public class UserStoreController {

    private final UserStoreService userStoreService;

    @Autowired
    public UserStoreController(UserStoreService userStoreService) {
        this.userStoreService = userStoreService;
    }

    @GetMapping("find/{storeId}")
    public ResponseEntity<?> findUserStoreIdByStoreId(@PathVariable Integer storeId) {
        Integer userStoreId = userStoreService.findUserStoreIdByStoreId(storeId);
        if (userStoreId == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(userStoreId);
    }

    @GetMapping("validate/{userStoreId}")
    public ResponseEntity<?> validateUserStoreId(@PathVariable Integer userStoreId) {
        boolean isValid = userStoreService.validateUserStoreId(userStoreId);
        if (!isValid) {
            return ResponseEntity.badRequest().body("user_store_id가 없습니다");
        }
        return ResponseEntity.ok("user_store_id가 유효합니다");
    }
}
