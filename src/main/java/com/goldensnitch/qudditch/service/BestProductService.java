package com.goldensnitch.qudditch.service;

import com.goldensnitch.qudditch.dto.UserStore;
import com.goldensnitch.qudditch.mapper.BestProductMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class BestProductService {
    private final BestProductMapper bestProductMapper;

    @Autowired
    public BestProductService(BestProductMapper bestProductMapper) {

        this.bestProductMapper = bestProductMapper;
    }

    public List<UserStore> BestProduct(Integer Id) {

        return bestProductMapper.BestProduct(Id);
    }

    public List<Map<String, Object>> bestProductByAge() {
        return bestProductMapper.bestProductByAge();
    }

    public List<Map<String, Object>> bestProductByGender() {
        return bestProductMapper.bestProductByGender();
    }
}
