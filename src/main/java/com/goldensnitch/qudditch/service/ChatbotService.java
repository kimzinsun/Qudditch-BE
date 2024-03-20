package com.goldensnitch.qudditch.service;

import com.goldensnitch.qudditch.dto.Chatbot;
import com.goldensnitch.qudditch.dto.Store;
import com.goldensnitch.qudditch.mapper.ChatbotMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ChatbotService {

    private final ChatbotMapper chatbotMapper;

    @Autowired
    public ChatbotService(ChatbotMapper chatbotMapper) {

        this.chatbotMapper = chatbotMapper;
    }

    public List<Chatbot> price(String name) {

        return chatbotMapper.price(name);
    }

    public List<Store> getNearProductStoreList(String productName, double currentWgs84X, double currentWgs84Y){

        return chatbotMapper.selectNearProductStores(productName, currentWgs84X, currentWgs84Y);
    }

    public List<String> getSimilarProductNameList(String productName){
        return chatbotMapper.selectSimilarProductNames(productName);
    }

    public List<Chatbot> random() {

        return chatbotMapper.random();
    }

   /* public List<ChatbotStore> store(String name) {

        return chatbotMapper.store(name);
    }*/

    public List<Store> getNearStoreList(double currentWgs84X, double currentWgs84Y){
        return chatbotMapper.selectNearStores(currentWgs84X, currentWgs84Y);
    }
}
