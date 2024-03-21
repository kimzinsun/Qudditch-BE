package com.goldensnitch.qudditch.controller;

import com.goldensnitch.qudditch.dto.payment.PaymentRequest;
import com.goldensnitch.qudditch.dto.payment.PaymentResponse;
import com.goldensnitch.qudditch.service.PaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/payment")
public class PaymentController {

    private final PaymentService paymentService;

    @Autowired
    public PaymentController(PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    @PostMapping("/initiate")
    public ResponseEntity<?> initiatePayment(@RequestBody PaymentRequest paymentRequest) {
        try {
            // 사용자로부터 받은 정보와 주문 ID를 기반으로 결제 초기화
            // 컨트롤러에 설정한 파라미터가 paymentService 메소드로 전달되어 기능 동작
            paymentRequest.setCid("TC0ONETIME");

            String redirectUrl = paymentService.initiatePayment(paymentRequest);

            if (!"Error".equals(redirectUrl)) {
                return ResponseEntity.ok().body(redirectUrl);
            } else {
                return ResponseEntity.badRequest().body("Failed to initiate payment");
            }
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Error initiating payment: " + e.getMessage());
        }
    }

    @GetMapping("/approve")
    public ResponseEntity<?> approvePayment(@RequestParam("pg_token") String pg_token, @RequestParam("order_id") String partnerOrderId) {
        try {
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
}
