package com.goldensnitch.qudditch.controller;

import com.goldensnitch.qudditch.dto.CustomerOrderProduct;
import com.goldensnitch.qudditch.service.CustomerOrderProductService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@RestController
@RequestMapping("/api/cart")
public class CustomerOrderProductController {

    private final CustomerOrderProductService customerOrderProductService;

    public CustomerOrderProductController(CustomerOrderProductService customerOrderProductService) {
        this.customerOrderProductService = customerOrderProductService;
    }

    @PostMapping("/add")
    public ResponseEntity<?> addItemToCart(@RequestBody CustomerOrderProduct addItemToCart) {
        try{
            customerOrderProductService.addItemToCustomerOrder(addItemToCart.getCustomerOrderId(),
                    addItemToCart.getProductId(),
                    addItemToCart.getQty());
            return ResponseEntity.ok().body("Item added to cart successfully.");
        }
        catch (Exception e){
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/{customerOrderId}")
    public ResponseEntity<?> getCustomerOrderDetails(@PathVariable Integer customerOrderId) {
        Map<Integer, Integer> orderDetails = customerOrderProductService.getCustomerOrderDetails(customerOrderId).getOrDefault(customerOrderId, new ConcurrentHashMap<>());
        return ResponseEntity.ok(orderDetails);
    }
}