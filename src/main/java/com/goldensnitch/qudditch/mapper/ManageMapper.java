package com.goldensnitch.qudditch.mapper;

import com.goldensnitch.qudditch.dto.manage.OrderDetailRes;
import com.goldensnitch.qudditch.dto.manage.OrderRes;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface ManageMapper {
    List<OrderRes> getOrderList();
    List<OrderDetailRes> getOrderDetail(int orderStoreId);
}
