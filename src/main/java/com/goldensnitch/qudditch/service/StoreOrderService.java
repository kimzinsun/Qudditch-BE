package com.goldensnitch.qudditch.service;

import com.goldensnitch.qudditch.dto.StoreOder.ProductWithDetailQty;
import com.goldensnitch.qudditch.dto.StoreOder.StoreOrderParam;
import com.goldensnitch.qudditch.dto.StoreOrder;
import com.goldensnitch.qudditch.dto.StoreOrderProduct;
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

    public List<StoreOrder> orderList(StoreOrderParam param, int currentPage) {
        return storeOrderMapper.orderList(param);
    }
    public int getallList(StoreOrderParam param) {
        return storeOrderMapper.getallList(param);
    }

    public int insertOrder(StoreOrder order) {
        return storeOrderMapper.insertOrder(order);
    }
    public Integer getStoreId() {return storeOrderMapper.getStoreId();}

    public int insertId(StoreOrderProduct product) {return storeOrderMapper.insertId(product);}

    public StoreOrder getStoreOrderById(int id) {
        return storeOrderMapper.getStoreOrderById(id);
    }

    public ProductWithDetailQty getProductWithQty(Integer id) {
        return storeOrderMapper.getProductWithQty(id);
    }

    public int updateOrderProducts(StoreOrderProduct product) {
        return storeOrderMapper.updateOrderProducts(product);}


}

