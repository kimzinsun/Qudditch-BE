package com.goldensnitch.qudditch.mapper;

import com.goldensnitch.qudditch.dto.CustomerOrder;
import com.goldensnitch.qudditch.dto.CustomerOrderProduct;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

@Mapper
public interface CustomerOrderProductMapper  {
    void insertCustomerOrder(CustomerOrder customerOrder);

    void insertCustomerOrderProduct(CustomerOrderProduct customerOrderProduct);

    // CustomerOrder findById(int id);

    List<CustomerOrder> findByUserCustomerId(String monthYear);

     List<CustomerOrder> findByMonthYear(String monthYear, Integer status);

     CustomerOrder findById(int customerOrderId);

    CustomerOrder findByPartnerOrderId(String partnerOrderId);

    void update(CustomerOrder order);

    CustomerOrder findByTid(String tid);

    List<CustomerOrderProduct> findOrderProductsByOrderId(int customerOrderId);

    List<CustomerOrderProduct> findOrderProductsByPartnerOrderId(String partnerOrderId);

    // 사용자 ID로 포인트 사용 및 적립 내역 조회
    List<CustomerOrder> findPointHistoryByCustomerId(@Param("userCustomerId") Integer userCustomerId);

    void updateOrderStatus(@Param("tid") Map<String, Object> tid);

    // List<CustomerOrder> findByMonthYear(Map<String, Object> params);
}
