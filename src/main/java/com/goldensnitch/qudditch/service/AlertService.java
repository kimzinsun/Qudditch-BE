package com.goldensnitch.qudditch.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.goldensnitch.qudditch.dto.CustomerAlertLog;
import com.goldensnitch.qudditch.mapper.AlertMapper;

@Service
public class AlertService {
    AlertMapper alertMapper;
    public AlertService(AlertMapper alertMapper) {
        this.alertMapper = alertMapper;
    }
    // 알림 목록을 조회
    public List<CustomerAlertLog> alertList(int id){
        return alertMapper.alertList(id);
    }
    // 알림 목록을 삭제
    public int deleteAlert(int id, int userCustomerId){
        return alertMapper.deleteAlert(id, userCustomerId);
    }
}
