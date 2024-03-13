package com.goldensnitch.qudditch.controller;

import com.goldensnitch.qudditch.dto.OrderRequest;
import com.goldensnitch.qudditch.service.CustomerOrderProductService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/order")
public class CustomerOrderProductController {

    private final CustomerOrderProductService customerOrderProductService;

    public CustomerOrderProductController(CustomerOrderProductService customerOrderProductService) {
        this.customerOrderProductService = customerOrderProductService;
    }

    @PostMapping("/add")
    public ResponseEntity<?> addOrder(@RequestBody OrderRequest orderRequest) {
        try{
            customerOrderProductService.createCustomerOrder(orderRequest.getCustomerOrder(), orderRequest.getCustomerOrderProducts());
            return ResponseEntity.ok().body("Item added to cart successfully.");
        }
        catch (Exception e){
            return ResponseEntity.internalServerError().body("Failed to create order: " + e.getMessage());
        }
    }

//    @GetMapping("/{customerOrderId}")
//    public ResponseEntity<?> getCustomerOrderDetails(@PathVariable Integer customerOrderId) {
//        Map<Integer, Integer> orderDetails = customerOrderProductService.getCustomerOrderDetails(customerOrderId);
//        return ResponseEntity.ok(orderDetails);
//    }
}