package com.goldensnitch.qudditch.service;

import com.goldensnitch.qudditch.dto.Product;
import com.goldensnitch.qudditch.dto.payment.CartItem;
import com.goldensnitch.qudditch.mapper.ProductMapper;
import com.goldensnitch.qudditch.mapper.StoreStockMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class CartService { // 장바구니 기능 (추가, 조회, 수량변경, 개별삭제, 모두삭제)

    private final UserStoreService userStoreService;
    private final ProductMapper productMapper;
    private final StoreStockMapper storeStockMapper;

    @Autowired
    public CartService(UserStoreService userStoreService, ProductMapper productMapper, StoreStockMapper storeStockMapper) {
        this.userStoreService = userStoreService;
        this.productMapper = productMapper;
        this.storeStockMapper = storeStockMapper;
    }

    private final Map<Integer, List<CartItem>> userCarts = new HashMap<>();
    // ConcurrentHashMap은 어떨 때 쓰이는지? 동시성이슈가 뭔지?

    // 장바구니 아이템 추가
    public boolean addItemToCart(Integer storeId, Integer userCustomerId, Integer productId) {
        List<CartItem> cart = userCarts.computeIfAbsent(userCustomerId, k -> new ArrayList<>());
        // computeIfAbsent: Map에 특정 키에 해당하는 키값이 존재하는지 확인하고 없으면 새로 만들어주는 코드

        // userStoreId 조회
        Integer userStoreId = userStoreService.selectUserStore(storeId);
        if (userStoreId == null) {
            throw new RuntimeException("선택한 가게를 찾을 수 없습니다.");
            // return false; // Store selection failed
        }

        // 상품 정보 조회
        Product product = productMapper.findByProductId(productId);
        if (product == null) {
            throw new RuntimeException("상품 정보를 찾을 수 없습니다.");
        }

        // 상품 수량 조회
        Integer qty = storeStockMapper.selectStockQtyByProductIdAndUserStoreId(productId, userStoreId);
        if (qty == null || qty <= 0) {
            throw new RuntimeException("선택한 상품의 재고가 없습니다.");
        }

        // CartItem 객체 생성 및 초기화
        CartItem item = new CartItem();
        item.setUserStoreId(userStoreId);
        item.setName(product.getName());
        item.setProductId(productId);
        item.setQty(1); // 수량이 1개씩 증가, 수량변경: updateItemQty 메서드
        item.setPrice(product.getPrice());

        cart.add(item);

        userCarts.put(userCustomerId, cart);
        return true;
    }

    // 장바구니 조회
    public List<CartItem> getCartItem(Integer userCustomerId){
        return userCarts.getOrDefault(userCustomerId, new ArrayList<>());
        // getOrDefault: 찾는 키가 존재하면 키값을 반환하고, 없으면 기본 값을 반환
        // Collections.emptyList: static으로 이미 만들어진 객체, 값을 변경해야하면 new ArrayList()를 사용하는 것이 편리
    }

    // 장바구니 아이템 수량 변경
    public void updateItemQty(int userCustomerId, CartItem item){
        List<CartItem> cartItems = userCarts.get(userCustomerId);
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
