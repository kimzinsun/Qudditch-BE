package com.goldensnitch.qudditch.dto;

import lombok.Data;

@Data
public class PaginationParam {
    private Integer page = 1;
    private Integer recordSize = 5;
    private Integer pageSize = 3;
    private Integer offset = 0;
//    private String keyword;

    public void setPage(Integer page) {
        this.page = page;
        this.offset = (page - 1) * recordSize;
    }

}
