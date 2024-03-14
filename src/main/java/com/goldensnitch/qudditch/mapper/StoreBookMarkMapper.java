package com.goldensnitch.qudditch.mapper;

import org.apache.ibatis.annotations.Mapper;

import java.util.Map;

@Mapper
public interface StoreBookMarkMapper {

    void addStoreBookmark(Map<String, Integer> params);

    void removeStoreBookmark(Map<String, Integer> params);



}
