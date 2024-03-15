package com.goldensnitch.qudditch.dto;

import lombok.Data;

import java.sql.Date;

@Data
public class CustomerOrder {
    private Integer id;
    private Integer userCustomerId;
    private Integer userStoreId; // 가맹점 코드
    private Integer totalAmount;
    private Integer usedPoint;
    private Integer totalPay;
    private Integer earnPoint;
    // private Date orderedAt;
    private Date orderedAt; // 서비스 코드에 java.time.LocalDate 또는 java.time.LocalDateTime 클래스에서 format(DateTimeFormatter) 메서드를 사용
    private String tid; // 결제 고유번호
}
