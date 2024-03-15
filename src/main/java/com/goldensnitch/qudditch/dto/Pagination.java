package com.goldensnitch.qudditch.dto;

import lombok.Data;

@Data
public class Pagination {
    private Integer totalRecordCount;
    private Integer totalPageCount;
    private Integer startPage;
    private Integer endPage;
    private Integer limitStart;
    private Boolean existPrev;
    private Boolean existNext;
    private PaginationParam paginationParam;

    public Pagination() {
    }

    public Pagination(Integer totalRecordCount, PaginationParam paginationParam) {
        if (totalRecordCount > 0) {
            this.totalRecordCount = totalRecordCount;
            this.paginationParam = paginationParam;
            calculatePagination();
        } else {
            this.totalRecordCount = 0;
            this.totalPageCount = 0;
            this.startPage = 0;
            this.endPage = 0;
            this.limitStart = 0;
            this.existPrev = false;
            this.existNext = false;
            this.paginationParam = paginationParam;
        }
    }

    private void calculatePagination() {
        this.totalPageCount = ((this.totalRecordCount) - 1) / paginationParam.getRecordSize() + 1;

        if (paginationParam.getPage() > totalPageCount) {
            paginationParam.setPage(totalPageCount);
        }

        this.startPage = ((paginationParam.getPage() - 1) / paginationParam.getPageSize()) * paginationParam.getPageSize() + 1;
        this.endPage = this.startPage + paginationParam.getPageSize() - 1;

        if (this.endPage > this.totalPageCount) {
            this.endPage = this.totalPageCount;
        }

        this.limitStart = (paginationParam.getPage() - 1) * paginationParam.getRecordSize();
        this.existPrev = this.startPage != 1;
        this.existNext = !this.endPage.equals(this.totalPageCount);
    }
}
