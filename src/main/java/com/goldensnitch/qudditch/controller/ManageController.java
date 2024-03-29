package com.goldensnitch.qudditch.controller;

import com.goldensnitch.qudditch.dto.Pagination;
import com.goldensnitch.qudditch.dto.PaginationParam;
import com.goldensnitch.qudditch.dto.manage.InputReq;
import com.goldensnitch.qudditch.service.ExtendedUserDetails;
import com.goldensnitch.qudditch.service.ManageService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/api/manage")
public class ManageController {
    private final ManageService manageService;

    public ManageController(ManageService manageService) {
        this.manageService = manageService;
    }

    @GetMapping("/order")
    public ResponseEntity<Map<String, Object>> getOrderList(PaginationParam paginationParam, @AuthenticationPrincipal ExtendedUserDetails userDetails) {
        Map<String, Object> response = new HashMap<>();
        Integer userStoreId = userDetails.getId();

        if (userStoreId != 2) {
            response.put("status", "fail");
            response.put("message", "잘못된 접근입니다");
        } else {
            int count = manageService.getOrderCount();
            if (count == 0) {
                response.put("status", "fail");
                response.put("message", "주문이 존재하지 않습니다.");
            } else {
                response.put("status", "success");
                response.put("data", manageService.getOrderList(paginationParam));
                response.put("pagination", new Pagination(count, paginationParam));

            }
        }

        return ResponseEntity.ok(response);

    }

    @GetMapping("/order/detail/{orderStoreId}")
    public ResponseEntity<Map<String, Object>> getOrderDetail(@PathVariable int orderStoreId) {
        Map<String, Object> response = new HashMap<>();

        if (manageService.getOrderDetail(orderStoreId).isEmpty()) {
            response.put("status", "fail");
            response.put("message", "주문 상세정보가 존재하지 않습니다.");
        } else {

            response.put("status", "success");
            response.put("data", manageService.getOrderDetail(orderStoreId));
        }
        return ResponseEntity.ok(response);
    }

    @PostMapping("/order/detail/{orderStoreId}")
    public ResponseEntity<Map<String, Object>> confirmOrder(@PathVariable int orderStoreId, @RequestBody List<InputReq> list) {
        Map<String, Object> response = new HashMap<>();
        manageService.confirmOrder(orderStoreId, list);

        response.put("status", "success");
        return ResponseEntity.ok(response);
    }

}
