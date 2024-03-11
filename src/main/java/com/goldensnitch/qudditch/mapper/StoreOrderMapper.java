package com.goldensnitch.qudditch.mapper;

import com.goldensnitch.qudditch.dto.StoreOder.ProductWithQty;
import com.goldensnitch.qudditch.dto.StoreOder.StoreOrderParam;
import com.goldensnitch.qudditch.dto.StoreOrder;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface StoreOrderMapper {
    List<StoreOrder> orderList(StoreOrderParam param);
    int getallList(StoreOrderParam param);
    int insertOrder(StoreOrder order);
    StoreOrder getStoreOrderById(int id);
    ProductWithQty getProductWithQty(Integer id);
    int updateOrder(int id);
    int updateOrderProducts(int id);

}
