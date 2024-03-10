package com.goldensnitch.qudditch.dto.graph;

import lombok.Data;

import java.util.List;

@Data
public class CategoryGraphDto {
    private List<CategorySalesDto> currentList;
    private List<CategorySalesDto> lastList;
}
