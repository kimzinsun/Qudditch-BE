package com.goldensnitch.qudditch.mapper;

import com.goldensnitch.qudditch.dto.Chatbot;
import com.goldensnitch.qudditch.dto.Store;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
@Mapper
public interface ChatbotMapper {

    List<Chatbot> price(String name);
    List<Store> selectNearProductStores(String productName, double currentWgs84X, double currentWgs84Y);
    List<String> selectSimilarProductNames(String productName);
    /*List<ChatbotStore> store(String name);*/

    List<Chatbot> random();
    List<Store> selectNearStores(double currentWgs84X, double currentWgs84Y);
}
