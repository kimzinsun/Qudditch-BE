package com.goldensnitch.qudditch.controller;

import com.goldensnitch.qudditch.dto.CustomerBookmarkProduct;
import com.goldensnitch.qudditch.service.BookmarkUserService;
import com.goldensnitch.qudditch.service.ExtendedUserDetails;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/api/bookmark/user")
public class BookmarkUserController {

    @Autowired
    BookmarkUserService service;

    @GetMapping("/products")
    public ResponseEntity<Map<String, Object>> getProduct(@AuthenticationPrincipal ExtendedUserDetails userDetails){
        Map<String, Object> map = new HashMap<>();
        int userCustomerId = userDetails.getId();

        log.info("BookmarkUserController.getProduct: userCustomerId {}", userCustomerId);

        List<CustomerBookmarkProduct> bookmarkList = service.getProducts(userCustomerId);

        map.put("bookmarkList", bookmarkList);

        if (bookmarkList.isEmpty()) {
            map.put("message", "해당 유저의 북마크 목록이 없습니다.");
        } else {
            map.put("message", "SUCCESS");
        }

        return ResponseEntity.ok().body(map);
    }

    @PostMapping("/product")
    public ResponseEntity<String> createProduct(@AuthenticationPrincipal ExtendedUserDetails userDetails, @RequestBody CustomerBookmarkProduct dto) {
        dto.setUserCustomerId(userDetails.getId());

        log.info("BookmarkUserController.createProduct: CustomerBookmarkProduct {}", dto);

        String message = "FAIL";

        if (service.isDuplicateProduct(dto)){
            message = "이미 추가된 북마크가 있습니다.";
        } else {
            service.createProduct(dto);
            message = "SUCCESS";
        }

        return ResponseEntity.ok().body(message);
    }

    @DeleteMapping("/product")
    public ResponseEntity<String> removeProduct(@AuthenticationPrincipal ExtendedUserDetails userDetails, CustomerBookmarkProduct dto) {
        dto.setUserCustomerId(userDetails.getId());

        log.info("BookmarkUserController.removeProduct: CustomerBookmarkProduct {}", dto);

        String message = "FAIL";
        boolean isSuccess = service.deleteProduct(dto);

        if (isSuccess) {
            message = "SUCCESS";
        } else {
            message = "북마크가 삭제되지 않았습니다.";
        }

        return ResponseEntity.ok().body(message);
    }
}
