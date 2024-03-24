package com.goldensnitch.qudditch.mapper;

import com.goldensnitch.qudditch.dto.CustomerOrder;
import com.goldensnitch.qudditch.dto.CustomerOrderProduct;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

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
}
