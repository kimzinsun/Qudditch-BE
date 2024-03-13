package com.goldensnitch.qudditch.controller;

import com.goldensnitch.qudditch.dto.CartItem;
import com.goldensnitch.qudditch.service.CartService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/cart")
public class CartController {

    private final CartService cartService;

    public CartController(CartService cartService) {
        this.cartService = cartService;
    }

    @PostMapping("/add")
    // PostMapping 에서 RequestParam을 사용해도 괜찮은지?
    public ResponseEntity<?> addItemToCart(@RequestParam("userCustomerId") Integer userCustomerId, @RequestBody CartItem cartItem){
        cartService.addItemToCart(userCustomerId, cartItem);
        return ResponseEntity.ok().body("Item added to cart successfully.");
    }

    @GetMapping("/get")
    public ResponseEntity<?> searchCartItem(@RequestParam("userCustomerId") Integer userCustomerId) {
        List<CartItem> cartItem = cartService.getCartItem(userCustomerId);

        if (cartItem == null || cartItem.isEmpty()) {
            return ResponseEntity.ok().body("Your cart is empty.");
        } else {
            return ResponseEntity.ok(cartItem);
        }
    }

    @PostMapping("/update")
    public ResponseEntity<?> updateItemQty(@RequestParam("userCustomerId") Integer userCustomerId, @RequestBody Integer productId, @RequestBody Integer qty){
        cartService.updateItemQty(userCustomerId, productId, qty);
        return ResponseEntity.ok().body("Cart item updated successfully.");
    }

    @PostMapping("/remove")
    public ResponseEntity<?> removeItemFromCart(@RequestParam("userCustomerId") Integer userCustomerId, @RequestBody Integer productId){
        cartService.removeItemFromCart(userCustomerId, productId);
        return ResponseEntity.ok().body("Item removed from cart successfully.");
    }
}

