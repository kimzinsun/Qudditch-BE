package com.goldensnitch.qudditch.controller;

import com.goldensnitch.qudditch.dto.PaymentRequest;
import com.goldensnitch.qudditch.service.PaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
}