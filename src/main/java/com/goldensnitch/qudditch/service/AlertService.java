package com.goldensnitch.qudditch.service;

import com.goldensnitch.qudditch.dto.CustomerAlertLog;
import com.goldensnitch.qudditch.mapper.AlertMapper;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AlertService {
    AlertMapper alertMapper;
    public AlertService(AlertMapper alertMapper) {
        this.alertMapper = alertMapper;
    }
    public List<CustomerAlertLog> alertList(int id){
        return alertMapper.alertList(id);
    }
    public int deleteAlert(int id, int userCustomerId){
        return alertMapper.deleteAlert(id, userCustomerId);
    }
}
