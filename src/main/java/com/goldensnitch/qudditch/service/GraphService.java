package com.goldensnitch.qudditch.service;

import com.goldensnitch.qudditch.dto.graph.DailySalesDto;
import com.goldensnitch.qudditch.dto.graph.SalesGraphDto;
import com.goldensnitch.qudditch.mapper.GraphMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GraphService {
    private final GraphMapper mapper;

    @Autowired
    public GraphService(GraphMapper mapper) {
        this.mapper = mapper;
    }

    public SalesGraphDto getSalesGraph(Integer userStoreId, String yearMonth){
        SalesGraphDto dto = new SalesGraphDto();

        // mysql date 포맷을 맞춰주기 위함
        yearMonth += "-01";

        List<DailySalesDto> list = mapper.selectSalesList(userStoreId, yearMonth);

        if (!list.isEmpty()){
            dto.setStart(list.get(0).getDate());
            dto.setEnd(list.get(list.size() - 1).getDate());
            dto.setList(list);
        }

        return dto;
    }
}
