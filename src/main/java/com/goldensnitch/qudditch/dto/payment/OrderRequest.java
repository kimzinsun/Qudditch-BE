package com.goldensnitch.qudditch.dto.payment;

import lombok.Data;

import java.util.List;

@Data
public class OrderRequest {
    private List<CartItem> cartItems;
    private Integer userCustomerId;
}


