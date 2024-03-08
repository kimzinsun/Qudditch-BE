package com.goldensnitch.qudditch.controller;

import com.goldensnitch.qudditch.dto.StoreOrderProduct;
import com.goldensnitch.qudditch.service.StoreOrderProductService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class StoreOrderProductController {

    private final StoreOrderProductService service;

    public StoreOrderProductController(StoreOrderProductService service) {
        this.service = service;
    }

    @GetMapping("/product/{productId}")
    public StoreOrderProduct getProductById(@PathVariable Integer productId){
        // QR 코드로 인식된 productId 사용하여 제품 조회
        return service.findByProductId(productId);
    }
    
    // 장바구니에 추가 및 결제 로직 관련 API
    
    // 해당 부분은 프로젝트의 요구사항에 맞게 구현

}
