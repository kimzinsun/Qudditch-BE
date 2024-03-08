package com.goldensnitch.qudditch.service;

import com.goldensnitch.qudditch.dto.StoreOrderProduct;
import com.goldensnitch.qudditch.mapper.StoreOrderProductMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class StoreOrderProductService {
    private final StoreOrderProductMapper mapper;

    @Autowired
    public StoreOrderProductService(StoreOrderProductMapper mapper) {
        this.mapper = mapper;
    }

    public StoreOrderProduct findByStoreProductId(Integer productId) {
        return mapper.findByStoreProductId(productId);
    }
}