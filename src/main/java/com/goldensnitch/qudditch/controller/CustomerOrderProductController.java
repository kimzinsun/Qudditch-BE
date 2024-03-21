package com.goldensnitch.qudditch.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/order")
public class CustomerOrderProductController {

//    @Autowired
//    private CustomerOrderProductService customerOrderProductService;
//
//    @GetMapping("/{orderId}/receipt")
//    public ResponseEntity<OrderResponse> generateReceipt(@PathVariable Integer orderId){
//        OrderResponse receipt = customerOrderProductService.generateReceipt(orderId);
//        return ResponseEntity.ok(receipt);
//    }
//
//    @GetMapping("/history/{userCustomerId}/{monthYear}")
//    public ResponseEntity<List<OrderResponse>> getMonthlyOrderHistory(@Param("userCustomerId") Integer userCustomerId, @Param("monthYear") String monthYear){
//        List<OrderResponse> history = customerOrderProductService.getMonthlyOrderHistory(userCustomerId, monthYear);
//        return ResponseEntity.ok(history);
//    }
}