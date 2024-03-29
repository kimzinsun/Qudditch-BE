package com.goldensnitch.qudditch.dto.fcm;

import lombok.Data;

@Data
public class FCMNotificationRequestDto {
    private int targetUserId;
    private String title;
    private String body;

}