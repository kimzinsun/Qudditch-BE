package com.goldensnitch.qudditch.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.goldensnitch.qudditch.dto.NotificationSetting;
import com.goldensnitch.qudditch.mapper.NotificationSettingMapper;

@Service
public class NotificationService {
    @Autowired
    private NotificationSettingMapper notificationSettingMapper;

    public NotificationSetting getNotificationSetting(Integer userId) {
        return notificationSettingMapper.getNotificationSettingByUserId(userId);
    }

    public void updateNotificationSetting(NotificationSetting setting) {
        notificationSettingMapper.updateNotificationSetting(setting);
    }
}
