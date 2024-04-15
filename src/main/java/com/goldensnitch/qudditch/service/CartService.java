package com.goldensnitch.qudditch.service;

import com.goldensnitch.qudditch.dto.Product;
import com.goldensnitch.qudditch.dto.payment.CartItem;
import com.goldensnitch.qudditch.mapper.ProductMapper;
import com.goldensnitch.qudditch.mapper.StoreStockMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
    public boolean addItemToCart(Integer storeId, Integer userCustomerId, Integer productId, Integer usedPoint) {
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

        // 장바구니에서 동일한 상품 찾기
        CartItem existingItem = cart.stream()
                .filter(item -> item.getProductId().equals(productId))
                .findFirst()
                .orElse(null);

        if(existingItem != null){
            // 동일한 상품이 이미 장바구니에 있는 경우 수량을 1증가
            existingItem.setQty(existingItem.getQty() + 1);
        }
        else {
            // CartItem 객체 생성 및 초기화
            CartItem item = new CartItem();
            item.setUserStoreId(userStoreId);
            item.setName(product.getName());
            item.setImage(product.getImage());
            item.setProductId(productId);
            item.setQty(1); // 수량이 1개씩 증가, 수량변경: updateItemQty 메서드
            item.setPrice(product.getPrice());

            // 부가세
            Integer taxFreeAmount = (int)(product.getPrice() * 0.9);
            item.setTax_free_amount(taxFreeAmount);
            item.setVat_amount(product.getPrice() - taxFreeAmount);

            // 포인트
            item.setEarnPoint((int) (product.getPrice() * 0.01));
            item.setUsedPoint(usedPoint);
            item.setTotalPay(product.getPrice() - usedPoint);
            cart.add(item);
        }

        userCarts.put(userCustomerId, cart);
        return true;
    }

    // 장바구니 조회
    public List<CartItem> getCartItem(Integer userCustomerId){
        // 기존에 userCarts에서 가져오던 방식 대신 새로운 메소드를 호출
        List<CartItem> cartItems = userCarts.getOrDefault(userCustomerId, new ArrayList<>());
        for (CartItem item : cartItems) {
            int stockQty = storeStockMapper.selectStockQtyByProductIdAndUserStoreId(item.getProductId(), item.getUserStoreId());
            item.setMaxQty(stockQty);  // CartItem 객체에 최대 수량 설정
        }
        return cartItems;
    }

    // 장바구니 아이템 수량 변경
    public void updateItemQty(int userCustomerId, CartItem item){
        List<CartItem> cartItems = userCarts.get(userCustomerId);
        if (cartItems != null) {
            for (CartItem cartItem : cartItems) {
                if (cartItem.getProductId().equals(item.getProductId())) {
                    int stockQty = storeStockMapper.selectStockQtyByProductIdAndUserStoreId(item.getProductId(), cartItem.getUserStoreId());
                    if (item.getQty() <= stockQty) {
                        cartItem.setQty(item.getQty());
                    } else {
                        String errorMessage = String.format("Requested quantity %d exceeds stock limit %d for product ID %d.",
                                item.getQty(), stockQty, item.getProductId());
                        System.err.println(errorMessage);

                        throw new RuntimeException("Requested quantity exceeds stock limit.");
                    }
                }
            }
        }
    }

    // 상품 정보 조회 시, 최대 수량도 함께 조회하여 설정하는 메소드 예시
    public List<CartItem> fetchCartItemsWithMaxQty(int userCustomerId) {
        List<CartItem> cartItems = userCarts.get(userCustomerId);
        for (CartItem item : cartItems) {
            int stockQty = storeStockMapper.selectStockQtyByProductIdAndUserStoreId(item.getProductId(), item.getUserStoreId());
            item.setMaxQty(stockQty);  // CartItem 객체에 최대 수량 설정
        }
        return cartItems;
    }

    // 장바구니 아이템 삭제
    public ResponseEntity<?> removeItemFromCart(Integer userCustomerId, Integer productId){
        List<CartItem> cartItems = userCarts.getOrDefault(userCustomerId, new ArrayList<>());

        // 삭제할 아이템을 찾아 제거
        boolean isRemoved = cartItems.removeIf(item -> item.getProductId().equals(productId));

        if (isRemoved) {
            // 아이템 삭제에 성공한 경우, 업데이트된 장바구니 정보를 반환
            return ResponseEntity.ok().body(cartItems);
        } else {
            // 아이템을 찾지 못했거나 삭제에 실패한 경우
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Item with ID " + productId + " not found in cart.");
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
