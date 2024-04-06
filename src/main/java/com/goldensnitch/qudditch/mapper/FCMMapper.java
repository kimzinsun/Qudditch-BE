package com.goldensnitch.qudditch.mapper;

import com.goldensnitch.qudditch.dto.CustomerAlertLog;
import com.goldensnitch.qudditch.dto.CustomerDevice;
import com.goldensnitch.qudditch.dto.UserCustomer;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface FCMMapper {
    CustomerDevice selectCustomerDevice(int customerId);
    int countCustomerDevice(int userCustomerId);
    int insertCustomerDevice(CustomerDevice dto);
    int updateCustomerDeviceToken(CustomerDevice dto);
    int updateCustomerLoggedIn(CustomerDevice dto);
    int updateCustomerState(CustomerDevice active);
    int deleteCustomerDevice(int userCustomerId);
    List<CustomerAlertLog> selectCustomerAlertLogs(int userCustomerId);
    int insertCustomerAlertLog(CustomerAlertLog dto);
    UserCustomer selectUserCustomerByEmail(String email);
}
