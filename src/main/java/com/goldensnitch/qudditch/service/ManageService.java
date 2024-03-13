package com.goldensnitch.qudditch.service;

import com.goldensnitch.qudditch.dto.manage.InputOrder;
import com.goldensnitch.qudditch.dto.manage.InputReq;
import com.goldensnitch.qudditch.dto.manage.OrderDetailRes;
import com.goldensnitch.qudditch.dto.manage.OrderRes;
import com.goldensnitch.qudditch.mapper.ManageMapper;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

    public void confirmOrder(int orderStoreId, List<InputReq> list) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Map<String, Object> map = new HashMap<>();
        LocalDate NOW = LocalDate.now();

        map.put("orderStoreId", orderStoreId);
        manageMapper.updateOrderState(orderStoreId);
        manageMapper.insertStoreInput(map);

        for (InputReq inputReq : list) {
            InputOrder inputOrder = new InputOrder();
            inputOrder.setStoreInputId((Integer) map.get("no"));
            inputOrder.setProductId(inputReq.getProductId());
            inputOrder.setQty(inputReq.getQty());

            LocalDate expirationDate = NOW.plusDays(inputReq.getExpirationDate());
            inputOrder.setExpirationDate(expirationDate.toString());

            manageMapper.insertStoreInputProduct(inputOrder);
        }

    }
}
