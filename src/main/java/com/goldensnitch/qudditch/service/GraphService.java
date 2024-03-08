package com.goldensnitch.qudditch.service;

import com.goldensnitch.qudditch.dto.graph.MonthlySalesDto;
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

    public int test(){
        return mapper.selectTest();
    }

    public SalesGraphDto getSalesGraph(Integer userStoreId){
        SalesGraphDto dto = new SalesGraphDto();
        List<MonthlySalesDto> list = mapper.selectSalesList(userStoreId);

        if (!list.isEmpty()){
            dto.setStart(list.get(0).getYearMonth());
            dto.setEnd(list.get(list.size() - 1).getYearMonth());
            dto.setList(list);
        }

        return dto;
    }
}
