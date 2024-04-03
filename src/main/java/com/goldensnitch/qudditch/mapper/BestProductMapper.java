package com.goldensnitch.qudditch.mapper;

import com.goldensnitch.qudditch.dto.UserStore;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface BestProductMapper {
    List<UserStore> BestProduct(Integer Id);
}
