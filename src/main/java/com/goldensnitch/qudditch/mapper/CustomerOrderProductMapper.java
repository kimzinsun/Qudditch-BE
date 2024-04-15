package com.goldensnitch.qudditch.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.goldensnitch.qudditch.dto.CustomerOrder;
import com.goldensnitch.qudditch.dto.CustomerOrderProduct;

@Mapper
public interface CustomerOrderProductMapper  {
    void insertCustomerOrder(CustomerOrder customerOrder);

    void insertCustomerOrderProduct(CustomerOrderProduct customerOrderProduct);

    // CustomerOrder findById(int id);

    List<CustomerOrder> findByUserCustomerId(@Param("userCustomerId") Integer userCustomerId, @Param("monthYear") String monthYear);

    CustomerOrder findById(int customerOrderId);

    CustomerOrder findByPartnerOrderId(String partnerOrderId);

    void update(CustomerOrder order);

    CustomerOrder findByTid(String tid);

    List<CustomerOrderProduct> findOrderProductsByOrderId(int customerOrderId);

    // 사용자 ID로 포인트 사용 및 적립 내역 조회
    List<CustomerOrder> findPointHistoryByCustomerId(@Param("userCustomerId") Integer userCustomerId);
}
