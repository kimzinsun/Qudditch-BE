package com.goldensnitch.qudditch.service;

import com.goldensnitch.qudditch.dto.payment.CartItem;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class CartService { // 장바구니 기능 (추가, 조회, 수량변경, 개별삭제, 모두삭제)
    private final Map<Integer, List<CartItem>> userCarts = new ConcurrentHashMap<>();
    private final StoreSelectionService storeSelectionService;

    @Autowired
    public CartService(StoreSelectionService storeSelectionService) {
        this.storeSelectionService = storeSelectionService;
    }

    // 장바구니 아이템 추가
    public boolean addItemToCart(Integer storeId, CartItem item) {
        Integer userStoreId = storeSelectionService.selectServiceStore(storeId);
        if (userStoreId == null) {
            return false; // Store selection failed
        }
        item.setUserStoreId(userStoreId);
        userCarts.computeIfAbsent(item.getUserCustomerId(), k -> new ArrayList<>()).add(item);
        // computeIfAbsent: Map에 특정 키에 해당하는 키값이 존재하는지 확인하고 없으면 새로 만들어주는 코드
        return true;
    }

    // 장바구니 조회
    public List<CartItem> getCartItem(Integer userCustomerId){
        return userCarts.getOrDefault(userCustomerId, new ArrayList<>());
        // getOrDefault: 찾는 키가 존재하면 키값을 반환하고, 없으면 기본 값을 반환
        // Collections.emptyList: static으로 이미 만들어진 객체, 값을 변경해야하면 new ArrayList()를 사용하는 것이 편리
    }

    // 장바구니 아이템 수량 변경
    public void updateItemQty(CartItem item){
        List<CartItem> cartItems = userCarts.get(item.getUserCustomerId());
        if (cartItems != null) {
            cartItems.forEach(cartItem -> {
                if(cartItem.getProductId().equals(item.getProductId())){
                    cartItem.setQty(item.getQty());
                }
            });
        }
    }

    // 장바구니 아이템 삭제
    public boolean removeItemFromCart(Integer userCustomerId, Integer productId){
        List<CartItem> cartItems = userCarts.get(userCustomerId);
        if(cartItems != null){
            return cartItems.removeIf(item -> item.getProductId().equals(productId));
        }
        return false;
    }

    // 장바구니 비우기
    public void clearCart(Integer userCustomerId){
        List<CartItem> cartItems = userCarts.get(userCustomerId);
        if(cartItems != null){
            cartItems.clear();
        }
    }
}
