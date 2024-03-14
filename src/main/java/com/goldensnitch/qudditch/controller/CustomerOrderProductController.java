package com.goldensnitch.qudditch.controller;

import com.goldensnitch.qudditch.dto.OrderRequest;
import com.goldensnitch.qudditch.service.CustomerOrderProductService;
import org.springframework.beans.factory.annotation.Autowired;
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
    public ResponseEntity<OrderRequest> generateReceipt(@PathVariable Integer orderId){
        OrderRequest receipt = customerOrderProductService.generateReceipt(orderId);
        return ResponseEntity.ok(receipt);
    }

    @GetMapping("/history/{userCustomerId}/{monthYear}")
    public ResponseEntity<List<OrderRequest>> getMonthlyOrderHistory(@PathVariable Integer userCustomerId, @PathVariable String monthYear){
        List<OrderRequest> history = customerOrderProductService.getMonthlyOrderHistory(userCustomerId, monthYear);
        return ResponseEntity.ok(history);
    }
}