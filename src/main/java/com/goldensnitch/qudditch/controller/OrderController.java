package com.goldensnitch.qudditch.controller;

import com.goldensnitch.qudditch.dto.payment.CartItem;
import com.goldensnitch.qudditch.service.OrderService;
import com.goldensnitch.qudditch.service.PaymentService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/orders")
// @CrossOrigin(origins = "*", allowedHeaders = "*") // CORS 설정, 실제 운영 환경에서는 보안을 고려하여 적절히 조정 필요
public class OrderController {

    private final OrderService orderService;
    private final PaymentService paymentService;

    public OrderController(OrderService orderService, PaymentService paymentService) {
        this.orderService = orderService;
        this.paymentService = paymentService;
    }

    @PostMapping("/create")
    public ResponseEntity<?> createOrder(@RequestBody List<CartItem> cartItems, @RequestParam Integer userCustomerId) {
        try {
            // 주문 생성 및 주문 ID 가져오기
            Integer orderId = orderService.createOrder(userCustomerId, cartItems);

//            // 생성된 주문 ID를 사용하여 결제 초기화
//            String paymentInitiationResult = paymentService.initiatePayment(
//                    paymentRequest.getCid(),
//                    paymentRequest.getPartner_order_id(),
//                    paymentRequest.getPartner_user_id(),
//                    paymentRequest.getItem_name(),
//                    paymentRequest.getQuantity(),
//                    paymentRequest.getTotal_amount(),
//                    paymentRequest.getTax_free_amount(),
//                    paymentRequest.getCancel_url(),
//                    paymentRequest.getFail_url(),
//                    orderId); // 수정: 결제 초기화에 주문 ID 전달
//
//            if (!"Error".equals(paymentInitiationResult)) {
//                return ResponseEntity.ok().body("결제 페이지 URL: " + paymentInitiationResult);
//            } else {
//                return ResponseEntity.badRequest().body("Failed to initiate payment");
//            }
//        } catch (Exception e) {
//            return ResponseEntity.internalServerError().body("Error creating order and initiating payment: " + e.getMessage());
//          }

            if (orderId != null) {
                return ResponseEntity.ok().body("주문 생성 성공. 주문 ID: " + orderId);
            } else {
                return ResponseEntity.badRequest().body("주문 생성 실패");
            }
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("주문 생성 중 에러 발생: " + e.getMessage());
        }
    }

//    @PostMapping("/approve/{orderId}")
//    public ResponseEntity<?> approveOrder(@PathVariable Integer orderId, @RequestParam String pgToken) {
//        try {
//            // 주문 승인 처리
//            String result = orderService.approvePayment(pgToken, orderId);
//
//            if (("주문이 성공적으로 처리되었습니다. 주문 번호: " + orderId).equals(result)) {
//                return ResponseEntity.ok().body(result);
//            } else {
//                return ResponseEntity.badRequest().body(result);
//            }
//        } catch (Exception e) {
//            return ResponseEntity.internalServerError().body("주문 승인 중 에러 발생: " + e.getMessage());
//        }
//    }
}
