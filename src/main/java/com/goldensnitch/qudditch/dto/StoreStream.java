package com.goldensnitch.qudditch.dto;

import lombok.Data;

@Data
public class StoreStream {
    private Integer id;
    private Integer userStoreId;
    private String videoStreamArn;
    private String SignalingChannelArn;
    private String StreamProcessorName;
}
