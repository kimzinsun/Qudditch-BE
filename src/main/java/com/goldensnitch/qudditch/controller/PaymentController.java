package com.goldensnitch.qudditch.controller;

import com.goldensnitch.qudditch.dto.payment.CartItem;
import com.goldensnitch.qudditch.dto.payment.PaymentResponse;
import com.goldensnitch.qudditch.service.ExtendedUserDetails;
import com.goldensnitch.qudditch.service.PaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("api/payment")
public class PaymentController {

    private final PaymentService paymentService;

    @Autowired
    public PaymentController(PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    // getId 변경 - 03.29
    @PostMapping("/initiate")
    public ResponseEntity<?> initiatePayment(@RequestBody List<CartItem> cartItems, @AuthenticationPrincipal ExtendedUserDetails userDetails) {
        try {
            int userCustomerId = userDetails.getId();

            String redirectUrl = paymentService.initiatePayment(cartItems, userCustomerId);
            if (!"Error".equals(redirectUrl)) {
                return ResponseEntity.ok().body(Map.of("redirectUrl", redirectUrl));
            }
            else {
                return ResponseEntity.badRequest().body("Failed to initiate payment");
            }
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Error initiating payment: " + e.getMessage());
        }
    }

    @PostMapping("/approve")
    public ResponseEntity<?> approvePayment(@RequestBody Map<String, String> payload) {
        try {
            String pg_token = payload.get("pg_token");
            String partnerOrderId = payload.get("order_id");
            PaymentResponse paymentResponse = paymentService.approvePayment(pg_token, partnerOrderId);
            return ResponseEntity.ok(paymentResponse);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Payment approval failed: " + e.getMessage());
        }
    }

    @PostMapping("/cancel")
    public ResponseEntity<?> cancelPayment(@RequestParam String tid,
                                           @RequestParam String cancelAmount,
                                           @RequestParam String cancelTaxFreeAmount) {
        try {
            PaymentResponse paymentResponse = paymentService.cancelPayment(tid, cancelAmount, cancelTaxFreeAmount);
            return ResponseEntity.ok().body(paymentResponse);
        } catch (Exception e) {
            // 로그 기록 등 예외 처리 로직
            return ResponseEntity.internalServerError().body("Error canceling payment: " + e.getMessage());
        }
    }

    @PostMapping("/orderInfo")
    public ResponseEntity<?> getOrderInfo(@RequestBody Map<String, String> payload) {
        try {
            // payload에서 tid 추출
            String tid = payload.get("tid");
            if (tid == null || tid.trim().isEmpty()) {
                throw new IllegalArgumentException("Transaction ID (tid) is required.");
            }

            // PaymentService를 사용하여 주문 정보를 조회
            PaymentResponse paymentResponse = paymentService.getOrderInfo(tid);

            // 성공적으로 정보를 가져온 경우, PaymentResponse 객체를 반환
            return ResponseEntity.ok(paymentResponse);
        } catch (Exception e) {
            // 예외가 발생한 경우, 에러 메시지와 함께 클라이언트에게 응답
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

}
