package com.goldensnitch.qudditch.mapper;

import org.apache.ibatis.annotations.Mapper;

import com.goldensnitch.qudditch.dto.NotificationSetting;

@Mapper
public interface NotificationSettingMapper {
    void updateNotificationSetting(NotificationSetting setting);
    NotificationSetting getNotificationSettingByUserId(Integer userCustomerId);
}
