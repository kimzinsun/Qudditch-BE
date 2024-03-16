package com.goldensnitch.qudditch.controller;

import com.goldensnitch.qudditch.dto.payment.CartItem;
import com.goldensnitch.qudditch.service.OrderService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/orders")
// @CrossOrigin(origins = "*", allowedHeaders = "*") // CORS 설정, 실제 운영 환경에서는 보안을 고려하여 적절히 조정 필요
public class OrderController {

    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping("")
    public ResponseEntity<?> createOrder(@RequestBody List<CartItem> cartItems) {
        String paymentRedirectUrl = orderService.createOrder(cartItems);

        if("장바구니가 비어 있습니다.".equals(paymentRedirectUrl)){
            return ResponseEntity.badRequest().body(paymentRedirectUrl);
        }
        else if("Error".equals(paymentRedirectUrl)){
            return ResponseEntity.internalServerError().body("An error occurred during the order creation process.");
        }
        else{
            // 성공적으로 주문이 생성되고 결제 페이지 URL이 반환된 경우
            return ResponseEntity.ok(paymentRedirectUrl);
        }
    }
}

