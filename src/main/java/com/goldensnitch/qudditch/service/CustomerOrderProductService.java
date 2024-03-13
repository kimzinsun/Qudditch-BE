package com.goldensnitch.qudditch.service;

import com.goldensnitch.qudditch.dto.CustomerOrderProduct;
import com.goldensnitch.qudditch.mapper.CustomerOrderProductMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CustomerOrderProductService {

    @Autowired
    private CustomerOrderProductMapper customerOrderProductMapper;

    public void processPaymentProducts(List<CustomerOrderProduct> orderProducts) {
        // 결제된 상품 정보를 DB에 저장하는 로직
        for (CustomerOrderProduct orderProduct : orderProducts) {
            customerOrderProductMapper.insertCustomerOrderProduct(orderProduct);
            // 여기서 재고 차감 로직을 추가할 수 있습니다(별도 기능, 지금은 구현하지 않음)
        }

        // 영수증 정보 생성
    }
}

