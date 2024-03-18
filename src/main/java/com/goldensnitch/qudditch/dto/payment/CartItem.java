package com.goldensnitch.qudditch.dto.payment;

import lombok.Data;

@Data
public class CartItem { /* 장바구니에 담길 아이템 관리 */
    private Integer productId;
    private Integer qty;
    private Integer userStoreId; // 가게 ID 추가
    private Integer userCustomerId; // 고객 ID 추가
    private String productName;
    private Integer productPrice;
}