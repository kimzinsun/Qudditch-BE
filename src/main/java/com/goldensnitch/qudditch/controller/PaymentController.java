package com.goldensnitch.qudditch.controller;

import com.goldensnitch.qudditch.dto.payment.PaymentRequest;
import com.goldensnitch.qudditch.dto.payment.PaymentResponse;
import com.goldensnitch.qudditch.service.PaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/payment")
public class PaymentController {

    private final PaymentService paymentService;

    @Autowired
    public PaymentController(PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    @PostMapping("/initiate")
    public ResponseEntity<?> initiatePayment(@RequestBody PaymentRequest paymentRequest) {
        try {
            String redirectUrl = paymentService.initiatePayment(
                    paymentRequest.getCid(),
                    paymentRequest.getPartner_order_id(),
                    paymentRequest.getPartner_user_id(),
                    paymentRequest.getItem_name(),
                    paymentRequest.getQuantity(),
                    paymentRequest.getTotal_amount(),
                    paymentRequest.getTax_free_amount(),
                    paymentRequest.getApproval_url(),
                    paymentRequest.getCancel_url(),
                    paymentRequest.getFail_url());

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
    public ResponseEntity<?> approvePayment(@RequestParam String pg_token) {
        // tid와 pg_token을 어떻게 관리할지
        // 예시 코드
        String tid = "결제 고유 번호"; // 실제 애플리케이션에서는 적절하게 관리 필요

        PaymentResponse paymentResponse = paymentService.approvePayment(tid, pg_token);
        if (paymentResponse != null) {
            // 결제 승인 성공 시 로직 구현
            return ResponseEntity.ok().body(paymentResponse);
        } else {
            // 결제 승인 실패 시 로직 구현
            return ResponseEntity.badRequest().body("Failed to approve payment");
        }
    }

    @PostMapping("/cancel")
    public ResponseEntity<?> cancelPayment(@RequestParam String tid,
                                           @RequestParam String cancelAmount,
                                           @RequestParam String cancelTaxFreeAmount) {
        try {
            PaymentResponse paymentResponse = paymentService.cancelPayment(tid, cancelAmount, cancelTaxFreeAmount);
            if (paymentResponse != null) {
                // 결제 취소가 성공적으로 이루어졌을 경우의 처리 로직
                return ResponseEntity.ok().body(paymentResponse);
            } else {
                // 결제 취소 요청이 실패했을 경우의 처리 로직
                return ResponseEntity.badRequest().body("Failed to cancel payment");
            }
        } catch (Exception e) {
            // 예외 처리 로직
            return ResponseEntity.internalServerError().body("Error canceling payment: " + e.getMessage());
        }
    }
}