package com.goldensnitch.qudditch.controller;

import com.goldensnitch.qudditch.service.StoreOrderProductService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
public class StoreOrderProductController {

    private final StoreOrderProductService storeOrderProductService;

    public StoreOrderProductController(StoreOrderProductService storeOrderProductService) {
        this.storeOrderProductService = storeOrderProductService;
    }

    @GetMapping("/product/details/{productId}")
    // 포스트맨 주소연결: {{API_URL}}/product/details/343
    public ResponseEntity<?> getProductDetailsByProductId(@PathVariable Integer productId){
        try{
            Map<String, Object> productDetails = storeOrderProductService.getProductDetailsByProductId(productId);
            return ResponseEntity.ok(productDetails);
        }catch (Exception e){
            return ResponseEntity.notFound().build();
        }
    }

}