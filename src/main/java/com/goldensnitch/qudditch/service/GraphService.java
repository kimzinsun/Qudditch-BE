package com.goldensnitch.qudditch.service;

import com.goldensnitch.qudditch.dto.graph.CategoryGraphDto;
import com.goldensnitch.qudditch.dto.graph.CategorySalesDto;
import com.goldensnitch.qudditch.dto.graph.DailySalesDto;
import com.goldensnitch.qudditch.dto.graph.SalesGraphDto;
import com.goldensnitch.qudditch.mapper.GraphMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class GraphService {
    private final GraphMapper mapper;

    @Autowired
    public GraphService(GraphMapper mapper) {
        this.mapper = mapper;
    }

    public SalesGraphDto getSalesGraph(int userStoreId, String yearMonth){
        SalesGraphDto dto = new SalesGraphDto();

        yearMonth = getMySqlDate(yearMonth);

        List<DailySalesDto> list = mapper.selectSalesList(userStoreId, yearMonth);

        if (!list.isEmpty()){
            dto.setStart(list.get(0).getDate());
            dto.setEnd(list.get(list.size() - 1).getDate());
            dto.setList(list);
        }

        return dto;
    }

    public CategoryGraphDto getCategoryGraph(int userStoreId, String yearMonth){
        CategoryGraphDto dto = new CategoryGraphDto();

        yearMonth = getMySqlDate(yearMonth);

        List<CategorySalesDto> currentMonthList = mapper.selectCategoryList(userStoreId, yearMonth);

        String lastYearMonth = getLastDate(yearMonth);
        List<CategorySalesDto> lastMonthList = mapper.selectCategoryList(userStoreId, lastYearMonth);

        dto.setCurrentList(currentMonthList);
        dto.setLastList(lastMonthList);

        return dto;
    }

    private String getMySqlDate(String yearMonth){
        String yearMonthDay;

        yearMonth += "-01";
        yearMonthDay = yearMonth;

        return yearMonthDay;
    }

    private String getLastDate(String currentDate){
        LocalDate date = LocalDate.parse(currentDate);
        LocalDate lastMonthDate = date.minusMonths(1);

        return lastMonthDate.toString();
    }
}
