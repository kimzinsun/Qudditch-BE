package com.goldensnitch.qudditch.utils;

import com.goldensnitch.qudditch.dto.Pagination;

public class PaginationUtil {
    public static String CreatePaginationList(Integer currentPage, Pagination pagination) {
        StringBuilder sb = new StringBuilder();
        if (pagination.getExistPrev() && pagination.getExistNext()) {
            for (int i = currentPage - 2; i <= currentPage + 2; i++) {
                sb.append("<li onclick='handlePagination(").append(i).append(")'>").append(i).append("</li>");
            }
        } else if (pagination.getExistPrev()) {
            for (int i = pagination.getEndPage() - 4; i <= pagination.getEndPage(); i++) {
                sb.append("<li onclick='handlePagination(").append(i).append(")'>").append(i).append("</li>");
            }
        } else if (pagination.getExistNext()) {
            for (int i = 1; i <= 5; i++) {
                sb.append("<li onclick='handlePagination(").append(i).append(")'>").append(i).append("</li>");
            }
        } else {
            for (int i = 1; i <= pagination.getTotalPageCount(); i++) {
                sb.append("<li onclick='handlePagination(").append(i).append(")'>").append(i).append("</li>");
            }
        }
        return sb.toString();
    }

}
