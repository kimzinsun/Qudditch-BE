package com.goldensnitch.qudditch.controller;

import com.goldensnitch.qudditch.service.OrderService;
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

    /*
    @PostMapping("/createOrder")
    public ResponseEntity<?> createOrder(@RequestBody OrderRequest orderRequest) {
        try {
            // OrderService를 통해 주문 생성 및 결제 프로세스를 시작합니다.
            String paymentRedirectUrl = orderService.createOrder(orderRequest);

            // 결제 페이지로 리디렉션하는 URL을 클라이언트에게 반환합니다.
            return ResponseEntity.ok().body(paymentRedirectUrl);
        } catch (Exception e) {
            // 예외 발생 시, 클라이언트에게 오류 메시지를 반환합니다.
            return ResponseEntity.internalServerError().body("Failed to create order and initiate payment: " + e.getMessage());
        }
    }

     */
}

