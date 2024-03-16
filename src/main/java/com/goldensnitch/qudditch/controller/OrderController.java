package com.goldensnitch.qudditch.controller;

import com.goldensnitch.qudditch.dto.payment.OrderRequest;
import com.goldensnitch.qudditch.service.OrderService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
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

    @PostMapping("")
    public ResponseEntity<?> createOrder(@RequestBody OrderRequest orderRequest) {
        try {
            // 시도: 주문 생성
            String orderResponse = orderService.createOrder(orderRequest.getCartItems(), orderRequest.getUserCustomerId());

            // 성공적으로 주문이 생성된 경우, 클라이언트에게 주문 번호를 포함한 메시지 반환
            return ResponseEntity.ok().body(orderResponse);
        } catch (IllegalArgumentException e) {
            // 잘못된 입력이나 장바구니가 비어 있는 경우
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (IllegalStateException e) {
            // 결제 승인 실패, 재고 부족 등 처리 중 발생한 예외 처리
            return ResponseEntity.internalServerError().body(e.getMessage());
        } catch (Exception e) {
            // 기타 예상치 못한 예외 처리
            return ResponseEntity.internalServerError().body("주문 처리 중 오류가 발생했습니다.");
        }
    }
}