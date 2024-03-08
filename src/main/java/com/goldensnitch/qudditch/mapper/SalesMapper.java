package com.goldensnitch.qudditch.mapper;

import com.goldensnitch.qudditch.dto.CustomerOrder;
import org.apache.ibatis.annotations.Mapper;

import java.sql.Date;
import java.util.List;

@Mapper
public interface SalesMapper {

    List<CustomerOrder> DailySales(Date date);

    List<CustomerOrder> MonthlySales(CustomerOrder dto);
}
