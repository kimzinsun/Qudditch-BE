package com.goldensnitch.qudditch.service;

import com.goldensnitch.qudditch.dto.Chatbot;
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
}
