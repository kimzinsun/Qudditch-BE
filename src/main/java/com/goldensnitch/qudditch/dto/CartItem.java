package com.goldensnitch.qudditch.dto;

import lombok.Data;

@Data
public class CartItem { /* 장바구니에 담길 아이템 관리 */
    private Integer productId;
    private Integer qty;
}
