package com.goldensnitch.qudditch.service;

import com.goldensnitch.qudditch.dto.graph.*;
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

    public SalesGraphDto getSalesGraph(Integer userStoreId, String yearMonth) {
        SalesGraphDto dto = new SalesGraphDto();

        yearMonth = getMySqlDate(yearMonth);

        List<DailySalesDto> list = mapper.selectSalesList(userStoreId, yearMonth);

        dto.setList(list);

        if (!list.isEmpty()) {
            dto.setStart(list.get(0).getDate());
            dto.setEnd(list.get(list.size() - 1).getDate());
        }

        return dto;
    }

    public CategoryGraphDto getCategoryGraph(Integer userStoreId, String yearMonth) {
        CategoryGraphDto dto = new CategoryGraphDto();

        yearMonth = getMySqlDate(yearMonth);

        List<CategorySalesDto> currentMonthList = mapper.selectCategoryList(userStoreId, yearMonth);

        String lastYearMonth = getLastDate(yearMonth);
        List<CategorySalesDto> lastMonthList = mapper.selectCategoryList(userStoreId, lastYearMonth);

        dto.setCurrentList(currentMonthList);
        dto.setLastList(lastMonthList);

        return dto;
    }

    public VisitorGraphDto getVisitorGraph(Integer userStoreId, String yearMonth) {
        VisitorGraphDto dto = new VisitorGraphDto();

        yearMonth = getMySqlDate(yearMonth);

        List<DailyVisitorDto> dayList = mapper.selectDailyVisitorList(userStoreId, yearMonth);

        dto.setList(dayList);

        if (!dayList.isEmpty()) {
            for (DailyVisitorDto dayDto : dayList) {
                String date = dayDto.getDate();

                List<DailyHourVisitorDto> hourList = mapper.selectDailyHourVisitorList(userStoreId, date);

                dayDto.setList(hourList);
            }
        }

        return dto;
    }

    private String getMySqlDate(String yearMonth) {
        String yearMonthDay;

        yearMonth += "-01";
        yearMonthDay = yearMonth;

        return yearMonthDay;
    }

    private String getLastDate(String currentDate) {
        LocalDate date = LocalDate.parse(currentDate);
        LocalDate lastMonthDate = date.minusMonths(1);

        return lastMonthDate.toString();
    }
}
