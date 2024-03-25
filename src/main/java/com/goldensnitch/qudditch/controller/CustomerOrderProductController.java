package com.goldensnitch.qudditch.controller;

import com.goldensnitch.qudditch.dto.CustomerOrder;
import com.goldensnitch.qudditch.dto.payment.OrderResponse;
import com.goldensnitch.qudditch.service.CustomerOrderProductService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/order")
public class CustomerOrderProductController {

    private final CustomerOrderProductService customerOrderProductService;

    public CustomerOrderProductController(CustomerOrderProductService customerOrderProductService) {
        this.customerOrderProductService = customerOrderProductService;
    }

    @GetMapping("/receipt/{orderId}")
    public ResponseEntity<?> generateReceipt(@PathVariable Integer orderId){
        try{
            OrderResponse order = customerOrderProductService.generateReceipt(orderId); // Adjust based on actual return type
            return ResponseEntity.ok(order);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/history")
    public ResponseEntity<List<?>> getMonthlyOrderHistory(@RequestParam Integer userCustomerId, @RequestParam String monthYear){
        try {
            List<OrderResponse> history = customerOrderProductService.getMonthlyOrderHistory(userCustomerId, monthYear);
            return ResponseEntity.ok(history);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(null);
        }
    }

    @GetMapping("/history/point/{userCustomerId}")
    public ResponseEntity<List<CustomerOrder>> getPointHistoryByCustomerId(@PathVariable Integer userCustomerId) {
        try {
            List<CustomerOrder> pointHistory = customerOrderProductService.getPointHistoryByCustomerId(userCustomerId);
            return ResponseEntity.ok(pointHistory);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(null);
        }
    }
}