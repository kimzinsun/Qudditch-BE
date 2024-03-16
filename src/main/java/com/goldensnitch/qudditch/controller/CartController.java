package com.goldensnitch.qudditch.controller;

import com.goldensnitch.qudditch.dto.payment.CartItem;
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

    @PostMapping("")
    public ResponseEntity<?> addItemToCart(@RequestBody CartItem cartItem){
        if (cartService.addItemToCart(cartItem.getUserStoreId(), cartItem)) {
            return ResponseEntity.ok().body("Item added to cart successfully.");
        } else {
            return ResponseEntity.badRequest().body("Failed to add item to cart.");
        }
    }

    @GetMapping("")
    public ResponseEntity<?> getCartItems(@RequestParam("userCustomerId") Integer userCustomerId) {
        List<CartItem> cartItems = cartService.getCartItem(userCustomerId);
        return ResponseEntity.ok(cartItems);
    }

    @PutMapping("")
    public ResponseEntity<?> updateItemQty(@RequestBody CartItem cartItem){
        cartService.updateItemQty(cartItem);
        return ResponseEntity.ok().body("Cart item updated successfully.");
    }

    @DeleteMapping("/{productId}")
    public ResponseEntity<?> removeItemFromCart(@PathVariable Integer productId, @RequestParam("userCustomerId") Integer userCustomerId) {
        boolean isRemoved = cartService.removeItemFromCart(userCustomerId, productId);
        if (isRemoved) {
            return ResponseEntity.ok().body("Item removed from cart successfully.");
        } else {
            return ResponseEntity.badRequest().body("Failed to remove item from cart.");
        }
    }

    @DeleteMapping("/clear")
    public ResponseEntity<?> clearCart(@RequestParam("userCustomerId") Integer userCustomerId){
        cartService.clearCart(userCustomerId);
        return ResponseEntity.ok().body("Cart cleared successfully.");
    }
}
