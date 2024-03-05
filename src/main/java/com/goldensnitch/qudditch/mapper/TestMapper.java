package com.goldensnitch.qudditch.mapper;

import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface TestMapper {
    String selectTest();
}
