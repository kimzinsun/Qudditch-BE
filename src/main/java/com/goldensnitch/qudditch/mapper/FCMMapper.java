package com.goldensnitch.qudditch.mapper;

import com.goldensnitch.qudditch.dto.CustomerAlertLog;
import com.goldensnitch.qudditch.dto.CustomerDevice;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface FCMMapper {
    CustomerDevice selectCustomerDevice(int customerId);
    int countCustomerDevice(int userCustomerId);
    int insertCustomerDevice(CustomerDevice dto);
    int updateCustomerDeviceToken(CustomerDevice dto);
    List<CustomerAlertLog> selectCustomerAlertLogs(int userCustomerId);
    int insertCustomerAlertLog(CustomerAlertLog dto);
}
