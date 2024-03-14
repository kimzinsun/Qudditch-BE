package com.goldensnitch.qudditch.mapper;

import com.goldensnitch.qudditch.dto.manage.InputOrder;
import com.goldensnitch.qudditch.dto.manage.OrderDetailRes;
import com.goldensnitch.qudditch.dto.manage.OrderRes;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface ManageMapper {
    List<OrderRes> getOrderList();
    List<OrderDetailRes> getOrderDetail(int orderStoreId);
    void updateOrderState(int orderStoreId);
    void insertStoreInput(Map<String, Object> map);
    void insertStoreInputProduct(InputOrder inputOrder);
    int getUserStoreIdByOrderId(int orderStoreId);


}
