package com.goldensnitch.qudditch.service;

import com.goldensnitch.qudditch.mapper.VisitorMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class VisitorService {
    private final VisitorMapper visitorMapper;

    public VisitorService(VisitorMapper visitorMapper) {
        this.visitorMapper = visitorMapper;
    }

    public Integer getDailyVisitor(Integer storeId, String date) {
        if (visitorMapper.getDailyVisitor(storeId, date) == null) {
            return 0;
        }
        return visitorMapper.getDailyVisitor(storeId, date);
    }

    public Integer getMonthVisitor(Integer storeId, String yearMonth) {
        if (visitorMapper.getMonthVisitor(storeId, yearMonth) == null) {
            return 0;
        }
        return visitorMapper.getMonthVisitor(storeId, yearMonth);
    }

    public List<Map<String, Object>> getAgeVisitor() {
        return visitorMapper.getAgeVisitor();
    }

    public List<Map<String, Object>> getGenderVisitor() {
        return visitorMapper.getGenderVisitor();
    }
}
