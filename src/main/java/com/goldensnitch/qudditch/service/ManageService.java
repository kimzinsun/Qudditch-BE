package com.goldensnitch.qudditch.service;

import com.goldensnitch.qudditch.dto.manage.OrderDetailRes;
import com.goldensnitch.qudditch.dto.manage.OrderRes;
import com.goldensnitch.qudditch.mapper.ManageMapper;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ManageService {
    private final ManageMapper manageMapper;

    public ManageService(ManageMapper manageMapper) {
        this.manageMapper = manageMapper;
    }

    public List<OrderRes> getOrderList() {
        return manageMapper.getOrderList();
    }


    public List<OrderDetailRes> getOrderDetail(int orderStoreId) {
        return manageMapper.getOrderDetail(orderStoreId);
    }
}
