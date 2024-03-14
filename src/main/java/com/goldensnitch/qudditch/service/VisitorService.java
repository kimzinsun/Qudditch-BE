package com.goldensnitch.qudditch.service;

import com.goldensnitch.qudditch.mapper.VisitorMapper;
import org.springframework.stereotype.Service;

@Service
public class VisitorService {
    private final VisitorMapper visitorMapper;

    public VisitorService(VisitorMapper visitorMapper) {
        this.visitorMapper = visitorMapper;
    }

    public Integer getDailyVisitor(int storeId, String date) {
        if (visitorMapper.getDailyVisitor(storeId, date) == null) {
            return 0;
        }
        return visitorMapper.getDailyVisitor(storeId, date);
    }

    public Integer getMonthVisitor(int storeId, String yearMonth) {
        if (visitorMapper.getMonthVisitor(storeId, yearMonth) == null) {
            return 0;
        }
        return visitorMapper.getMonthVisitor(storeId, yearMonth);
    }
}
