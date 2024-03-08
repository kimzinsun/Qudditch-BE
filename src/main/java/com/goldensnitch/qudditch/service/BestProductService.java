package com.goldensnitch.qudditch.service;

import com.goldensnitch.qudditch.dto.StoreStockReport;
import com.goldensnitch.qudditch.mapper.BestProductMapper;
import com.goldensnitch.qudditch.mapper.SalesMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BestProductService {
    private final BestProductMapper  bestProductMapper;

    @Autowired
    public BestProductService(BestProductMapper bestProductMapper) {

        this.bestProductMapper = bestProductMapper;
    }

    public List<StoreStockReport> BestProduct(Integer storeId){

        return bestProductMapper.BestProduct(storeId);
    }
}
