package com.goldensnitch.qudditch.dto;

import lombok.Data;

import java.sql.Date;

@Data
public class kakaoReadyResponse {
    private Integer tid;
    private String next_redirect_mobile_url;
    private Date created_at;
}
