package com.goldensnitch.qudditch.dto;

import lombok.Data;

@Data
public class PaginationParam {
    private Integer page;
    private Integer recordSize;
    private Integer pageSize;
    private Integer offset;
    private String keyword;
    private String searchType;
    private Integer userId;

    public PaginationParam() {
        this.page = 1;
        this.recordSize = 20;
        this.pageSize = 5;
        this.offset = 0;
        this.keyword = "";
        this.searchType = "efcyQes";
    }

    public void setPage(Integer page) {
        this.page = page;
        this.offset = (page - 1) * recordSize;
    }
}
