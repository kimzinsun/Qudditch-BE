package com.goldensnitch.qudditch.controller;

import com.goldensnitch.qudditch.dto.CustomerOrder;
import com.goldensnitch.qudditch.dto.payment.OrderResponse;
import com.goldensnitch.qudditch.service.CustomerOrderProductService;
import com.goldensnitch.qudditch.service.ExtendedUserDetails;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/order")
public class CustomerOrderProductController {

    private final CustomerOrderProductService customerOrderProductService;

    public CustomerOrderProductController(CustomerOrderProductService customerOrderProductService) {
        this.customerOrderProductService = customerOrderProductService;
    }

    @GetMapping("/receipt")
    public ResponseEntity<?> getReceiptByPartnerOrderId(@RequestParam String partnerOrderId) {
        try {
            OrderResponse orderResponse = customerOrderProductService.generateReceiptByPartnerOrderId(partnerOrderId);
            return ResponseEntity.ok(orderResponse);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Order not found for partner_order_id: " + partnerOrderId);
        }
    }

    // getId 변경 - 03.29
    @GetMapping("/history")
    public ResponseEntity<List<?>> getMonthlyOrderHistory(@RequestParam String monthYear, @RequestParam Integer status){
        try {

            List<OrderResponse> history = customerOrderProductService.getMonthlyOrderHistory(monthYear, status);
            return ResponseEntity.ok(history);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(null);
        }
    }

    @GetMapping("/history/point/{userCustomerId}")
    public ResponseEntity<List<CustomerOrder>> getPointHistoryByCustomerId(@AuthenticationPrincipal ExtendedUserDetails userDetails) {
        try {
            int userCustomerId = userDetails.getId();

            List<CustomerOrder> pointHistory = customerOrderProductService.getPointHistoryByCustomerId(userCustomerId);
            return ResponseEntity.ok(pointHistory);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(null);
        }
    }
}