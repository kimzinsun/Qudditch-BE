package com.goldensnitch.qudditch.mapper;

import com.goldensnitch.qudditch.dto.CustomerAlertLog;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface AlertMapper {

    List<CustomerAlertLog> alertList(int id);

    int deleteAlert(int id, int userCustomerId);
}
