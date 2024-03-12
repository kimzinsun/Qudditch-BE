package com.goldensnitch.qudditch.dto.fcm;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class FCMNotificationRequestDto {
    private int targetUserId;
    private String title;
    private String body;

    @Builder
    public FCMNotificationRequestDto(int targetUserId, String title, String body) {
        this.targetUserId = targetUserId;
        this.title = title;
        this.body = body;
    }
}