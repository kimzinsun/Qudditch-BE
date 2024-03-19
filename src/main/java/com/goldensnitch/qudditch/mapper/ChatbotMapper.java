package com.goldensnitch.qudditch.mapper;

import com.goldensnitch.qudditch.dto.Chatbot;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
@Mapper
public interface ChatbotMapper {

    List<Chatbot> price(String name);
    List<Chatbot> store(String name);
}
