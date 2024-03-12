package com.goldensnitch.qudditch.mapper;

import com.goldensnitch.qudditch.dto.CustomerOrderProduct;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface CustomerOrderProductMapper  {
    void addItemToCustomerOrder(@Param("customerOrderId") Integer customerOrderId,
                                @Param("productId") Integer productId,
                                @Param("quantity") Integer qty);

    // 규원 작성(아래)
    List<CustomerOrderProduct> getCustomerOrderDetails(@Param("customerOrderId") Integer customerOrderId);
}
