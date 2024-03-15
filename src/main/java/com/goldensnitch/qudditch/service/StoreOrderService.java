package com.goldensnitch.qudditch.service;

import com.goldensnitch.qudditch.dto.PaginationParam;
import com.goldensnitch.qudditch.dto.StoreOder.ProductWithDetailQty;
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

    public List<StoreOrder> orderList(PaginationParam paginationParam) {
        return storeOrderMapper.orderList(paginationParam.getRecordSize(), paginationParam.getOffset());
    }
    public int cntOrderList() {
        return storeOrderMapper.cntOrderList();
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

