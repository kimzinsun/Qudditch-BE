package com.goldensnitch.qudditch.mapper;

import com.goldensnitch.qudditch.dto.PaymentRequest;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.data.repository.query.Param;

@Mapper
public interface PaymentMapper {
    void savePaymentRequest(@Param("paymentRequest") PaymentRequest paymentRequest);
}
