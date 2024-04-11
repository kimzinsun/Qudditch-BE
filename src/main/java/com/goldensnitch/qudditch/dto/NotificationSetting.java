package com.goldensnitch.qudditch.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class NotificationSetting {
    private Integer id;
    private Integer userCustomerId;
    private boolean emailNotificationEnabled;
    // 추가 필드 정의
}
