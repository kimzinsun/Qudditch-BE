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

    CustomerOrder findById(@Param("id") Integer id);

    List<CustomerOrder> findByUserCustomerId(@Param("userCustomerId") Integer userCustomerId);

    List<CustomerOrderProduct> findByOrderId(@Param("orderId") Integer orderId);
}
