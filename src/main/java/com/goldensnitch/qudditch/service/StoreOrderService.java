package com.goldensnitch.qudditch.service;

import com.goldensnitch.qudditch.dto.StoreOder.ProductWithQty;
import com.goldensnitch.qudditch.dto.StoreOder.StoreOrderParam;
import com.goldensnitch.qudditch.dto.StoreOrder;
import com.goldensnitch.qudditch.mapper.StoreOrderMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class StoreOrderService {
    private final StoreOrderMapper storeOrderMapper;

    @Autowired
    public  StoreOrderService(StoreOrderMapper storeOrderMapper) {
        this.storeOrderMapper = storeOrderMapper;
    }

    public List<StoreOrder> orderList(StoreOrderParam param) {
        return storeOrderMapper.orderList(param);
    }
    public int getallList(StoreOrderParam param) {
        return storeOrderMapper.getallList(param);
    }

    public int insertOrder(StoreOrder order) {
        return storeOrderMapper.insertOrder(order);
    }

    public StoreOrder getStoreOrderById(int id) {
        return storeOrderMapper.getStoreOrderById(id);
    }

    public ProductWithQty getProductWithQty(Integer id) {
        return storeOrderMapper.getProductWithQty(id);
    }

    public int updateOrder(int id) {
        return storeOrderMapper.updateOrder(id);
    }

    public int updateOrderProducts(int id) {
        return storeOrderMapper.updateOrderProducts(id);
    }

}

