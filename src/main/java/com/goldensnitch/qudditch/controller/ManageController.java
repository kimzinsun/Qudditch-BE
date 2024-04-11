package com.goldensnitch.qudditch.controller;

import com.goldensnitch.qudditch.dto.Pagination;
import com.goldensnitch.qudditch.dto.PaginationParam;
import com.goldensnitch.qudditch.dto.Product;
import com.goldensnitch.qudditch.dto.manage.InputReq;
import com.goldensnitch.qudditch.service.ExtendedUserDetails;
import com.goldensnitch.qudditch.service.ManageService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Nullable;
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
        Integer adminId = userDetails.getId();

        if (adminId != 9999) {
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

    @GetMapping("/store")
    public ResponseEntity<Map<String, Object>> getStoreList(PaginationParam paginationParam) {
        Map<String, Object> response = new HashMap<>();
        int count = manageService.getStoreCount();
        if (count == 0) {
            response.put("status", "fail");
            response.put("message", "점포가 존재하지 않습니다.");
        } else {
            response.put("status", "success");
            response.put("data", manageService.getStoreList(paginationParam));
            response.put("pagination", new Pagination(count, paginationParam));
        }

        return ResponseEntity.ok(response);
    }

    @GetMapping("/product")
    public ResponseEntity<Map<String, Object>> getProductList(PaginationParam paginationParam, @Nullable @RequestParam Integer categoryId) {
        Map<String, Object> response = new HashMap<>();

        if (categoryId == null) {
            int count = manageService.getProductCount();
            response.put("data", manageService.getProductList(paginationParam));
            response.put("pagination", new Pagination(count, paginationParam));
            response.put("status", "success");
        } else {
            int count = manageService.getProductCountByCategoryId(categoryId);
            response.put("data", manageService.getProductListByCategoryId(paginationParam, categoryId));
            response.put("pagination", new Pagination(count, paginationParam));
            response.put("status", "success");

        }
        return ResponseEntity.ok(response);
    }

    @PostMapping("/product")
    public ResponseEntity<Map<String, Object>> addProduct(@RequestBody Product product) {
        Map<String, Object> response = new HashMap<>();
        manageService.addProduct(product);
        response.put("status", "success");
        return ResponseEntity.ok(response);
    }

    @PostMapping("/product/{productId}")
    public ResponseEntity<Map<String, Object>> updateProduct(@PathVariable int productId, @RequestBody Product product) {
        Map<String, Object> response = new HashMap<>();
        manageService.updateProduct(productId, product);
        response.put("status", "success");
        return ResponseEntity.ok(response);
    }

}
