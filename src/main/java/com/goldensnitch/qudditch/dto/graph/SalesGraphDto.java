package com.goldensnitch.qudditch.dto.graph;

import lombok.Data;

import java.util.List;

@Data
public class SalesGraphDto {
    private List<MonthlySalesDto> list;
    private String start;
    private String end;
}
