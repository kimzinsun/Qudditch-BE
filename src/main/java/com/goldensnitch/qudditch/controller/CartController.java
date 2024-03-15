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
    public ResponseEntity<?> addItemToCart(@RequestBody CartItem cartItem){
        cartService.addItemToCart(cartItem);
        return ResponseEntity.ok().body("Item added to cart successfully.");
    }

    @GetMapping("/get")
    public ResponseEntity<?> getCartItems(@RequestParam("userCustomerId") Integer userCustomerId) {
        List<CartItem> cartItems = cartService.getCartItem(userCustomerId);

        if (cartItems == null || cartItems.isEmpty()) {
            return ResponseEntity.ok().body("Your cart is empty.");
        } else {
            return ResponseEntity.ok(cartItems);
        }
    }

    @PostMapping("/update")
    public ResponseEntity<?> updateItemQty(@RequestBody CartItem cartItem){
        cartService.updateItemQty(cartItem);
        return ResponseEntity.ok().body("Cart item updated successfully.");
    }

    @PostMapping("/remove")
    public ResponseEntity<?> removeItemFromCart(@RequestParam("userCustomerId") Integer userCustomerId, @RequestParam("productId") Integer productId){
        cartService.removeItemFromCart(userCustomerId, productId);
        return ResponseEntity.ok().body("Item removed from cart successfully.");
    }

    @PostMapping("/clear")
    public ResponseEntity<?> clearCart(@RequestParam("userCustomerId") Integer userCustomerId){
        cartService.clearCart(userCustomerId);
        return ResponseEntity.ok().body("Cart cleared successfully.");
    }
}
