package com.goldensnitch.qudditch.controller;

import com.goldensnitch.qudditch.service.OrderService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/orders")
// @CrossOrigin(origins = "*", allowedHeaders = "*") // CORS 설정, 실제 운영 환경에서는 보안을 고려하여 적절히 조정 필요
public class OrderController {

    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping("/{userCustomerId}")
    public ResponseEntity<?> createOrder(@PathVariable Integer userCustomerId) {
        String orderResult = orderService.createOrder(userCustomerId);

        if(orderResult.startsWith("주문이 성공적으로 처리되었습니다")) {
            return ResponseEntity.ok(orderResult);
        } else {
            return ResponseEntity.badRequest().body(orderResult);
        }
    }
}