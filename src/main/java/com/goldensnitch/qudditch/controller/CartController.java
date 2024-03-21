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
    public ResponseEntity<?> addItemToCart(@RequestParam Integer storeId, @RequestParam Integer userCustomerId, @RequestParam Integer productId){
        try{
            boolean addItemSuccess = cartService.addItemToCart(storeId, userCustomerId, productId);
            if(!addItemSuccess){
                return ResponseEntity.badRequest().body("Failed to add item to cart.");
            }

        // 장바구니 아이템 추가 후, 현재 장바구니 조회
        List<CartItem> currentCartItems = cartService.getCartItem(userCustomerId);

        // 현재 장바구니 상태 응답
        return ResponseEntity.ok(currentCartItems);
        } catch (Exception e){
            // 예외 발생
            return ResponseEntity.badRequest().body("Error adding item: " + e.getMessage());
        }
    }

    @GetMapping("")
    public ResponseEntity<?> getCartItems(@RequestParam("userCustomerId") Integer userCustomerId) {
        List<CartItem> cartItems = cartService.getCartItem(userCustomerId);
        return ResponseEntity.ok(cartItems);
    }

    @PutMapping("")
    public ResponseEntity<?> updateItemQty(@RequestParam Integer userCustomerId, @RequestBody CartItem cartItem){
        cartService.updateItemQty(userCustomerId, cartItem);
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
