package com.goldensnitch.qudditch.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class CustomerOrder {
    private Integer id;
    private Integer partnerOrderId;
    private Integer userCustomerId;
    private Integer userStoreId;
    private Integer totalAmount;
    private Integer usedPoint;
    private Integer totalPay;
    private Integer earnPoint;
    // private Date orderedAt;
    private LocalDateTime orderedAt;
    private String tid; // 결제 고유번호
    private String cid;
    private String pgToken;
}
