package com.goldensnitch.qudditch.dto.graph;

import lombok.Data;

import java.util.List;

@Data
public class DailyVisitorDto {
    private String date;
    private Integer count;
    private List<DailyHourVisitorDto> list;
}
