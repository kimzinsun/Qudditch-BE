package com.goldensnitch.qudditch.service;

import com.goldensnitch.qudditch.dto.CartItem;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class CartService {
    private final Map<Integer, List<CartItem>> userCarts = new ConcurrentHashMap<>();

    // 장바구니 아이템 추가
    public void addItemToCart(Integer userCustomerId, CartItem item){
        userCarts.computeIfAbsent(userCustomerId, k -> new ArrayList<>()).add(item);
        // computeIfAbsent: Map에 특정 키에 해당하는 키값이 존재하는지 확인하고 없으면 새로 만들어주는 코드
    }

    // 장바구니 조회
    public List<CartItem> getCartItem(Integer userCustomerId){
        return userCarts.getOrDefault(userCustomerId, Collections.emptyList());
        // getOrDefault: 찾는 키가 존재하면 키값을 반환하고, 없으면 기본 값을 반환
        // Collections.emptyList: static으로 이미 만들어진 객체, 값을 변경해야하면 new ArrayList()를 사용하는 것이 편리
    }

    // 장바구니 아이템 수량 변경
    public void updateItemQty(Integer userCustomerId, Integer productId, Integer qty){
        List<CartItem> cartItem = userCarts.get(userCustomerId);
        if(cartItem != null){
            cartItem.forEach(item -> {
                if(item.getProductId().equals(productId)){
                    item.setQty(qty);
                }
            });
        }
    }

    // 장바구니 아이템 삭제
    public void removeItemFromCart(Integer userCustomerId, Integer productId){
        List<CartItem> cartItems = userCarts.get(userCustomerId);
        if(cartItems != null){
            cartItems.removeIf(item -> item.getProductId().equals(productId));
        }
    }

    // 장바구니 비우기
    public void clearCart(Integer userCustomerId){
        List<CartItem> cartItems = userCarts.get(userCustomerId);
        if(cartItems != null){
            cartItems.clear();
        }
    }
}
