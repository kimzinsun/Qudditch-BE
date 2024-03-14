package com.goldensnitch.qudditch.mapper;

import com.goldensnitch.qudditch.dto.StoreOder.ProductWithDetailQty;
import com.goldensnitch.qudditch.dto.StoreOder.StoreOrderParam;
import com.goldensnitch.qudditch.dto.StoreOrder;
import com.goldensnitch.qudditch.dto.StoreOrderProduct;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface StoreOrderMapper {
    List<StoreOrder> orderList(StoreOrderParam param);
    int getallList(StoreOrderParam param);
    int insertOrder(StoreOrder order);
    Integer getStoreId();
    int insertId(StoreOrderProduct product);
    StoreOrder getStoreOrderById(int id);
    ProductWithDetailQty getProductWithQty(Integer id);
    int updateOrderProducts(StoreOrderProduct product);


}
