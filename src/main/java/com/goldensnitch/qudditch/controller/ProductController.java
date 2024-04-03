package com.goldensnitch.qudditch.controller;

import com.goldensnitch.qudditch.dto.*;
import com.goldensnitch.qudditch.service.ProductService;
import com.sendgrid.Response;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@SuppressWarnings("unused")
@Slf4j
@RestController
@RequestMapping("/api/product")
public class ProductController {
    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;

    }


    @GetMapping("/find/{productName}")
    public ResponseEntity<Map<String, Object>> selectProductByName(@PathVariable String productName) {
        List<Product> productList = productService.selectProductByName(productName);
        String status = productList.isEmpty() ? "fail" : "success";


        Map<String, Object> response = new HashMap<>();
        response.put("status", status);

        if (productList.isEmpty()) {
            response.put("message", "해당 상품이 존재하지 않습니다.");
        } else {
            response.put("data", productList);
        }

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    // selectProductByName로 먼저 이름을 검색해서 productId를 가져온 후에 selectStoreStockByProductId로 호출하는 방식으로 변경

    @GetMapping("/detail/{productId}")
    public ProductExt selectProductById(@PathVariable Integer productId) {
//        Integer userId = (Integer) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        int userId = 2;
        return productService.selectProductById(productId, userId);
    }

    @GetMapping("/store/{productId}")
    public ResponseEntity<Map<String, Object>> selectStoreStockByProductId(@PathVariable Integer productId, PaginationParam paginationParam, @RequestParam double currentWgs84X, @RequestParam double currentWgs84Y) {
        Map<String, Object> response = new HashMap<String, Object>();
        String status;
//        Integer userId = (Integer) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        Integer userId = null;
        ProductExt productDetail = productService.selectProductById(productId, userId);
        response.put("productDetail", productDetail);
        int count = productService.cntStoreStockByProductId(productId);

        if (currentWgs84X == 0 || currentWgs84Y == 0) {
            currentWgs84X = 129.1613;
            currentWgs84Y = 35.16018;
        }

        if (productDetail == null) {
            status = "fail";
            response.put("message", "상품을 찾을 수 없습니다.");
            response.put("status", status);
            return ResponseEntity.status(HttpStatus.OK).body(response);
        }
        if (productId == null || count == 0) {
            status = "success";
            response.put("message", "검색 결과가 없습니다.");
            response.put("status", status);
            return ResponseEntity.status(HttpStatus.OK).body(response);
        } else {
            status = "success";
            List<StoreStock> storeStockList = productService.selectStoreStockByProductId(productId, currentWgs84X, currentWgs84Y, paginationParam);
            Pagination pagination = new Pagination(count, paginationParam);

            response.put("status", status);
            response.put("pagination", pagination);
            response.put("data", storeStockList);

            return ResponseEntity.status(HttpStatus.OK).body(response);
        }

    }

    @GetMapping("/rank")
    public List<ProductRank> getBestProductList() {
        System.out.println(productService.getBestProductList());
        return productService.getBestProductList();
    }

}
