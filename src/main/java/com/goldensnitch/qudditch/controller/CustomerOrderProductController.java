package com.goldensnitch.qudditch.controller;

import com.goldensnitch.qudditch.dto.payment.OrderResponse;
import com.goldensnitch.qudditch.service.CustomerOrderProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/order")
public class CustomerOrderProductController {

    @Autowired
    private CustomerOrderProductService customerOrderProductService;

    @GetMapping("/{orderId}/receipt")
    public ResponseEntity<OrderResponse> generateReceipt(@PathVariable Integer orderId){
        OrderResponse receipt = customerOrderProductService.generateReceipt(orderId);
        return ResponseEntity.ok(receipt);
    }

    @GetMapping("/history/{userCustomerId}/{monthYear}")
    public ResponseEntity<List<OrderResponse>> getMonthlyOrderHistory(@Param("userCustomerId") Integer userCustomerId, @Param("monthYear") String monthYear){
        List<OrderResponse> history = customerOrderProductService.getMonthlyOrderHistory(userCustomerId, monthYear);
        return ResponseEntity.ok(history);
    }
}