package com.goldensnitch.qudditch.controller;

import com.goldensnitch.qudditch.dto.Chatbot;
import com.goldensnitch.qudditch.naver.NaverCloud;
import com.goldensnitch.qudditch.service.ChatbotService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/api/chatbot")
public class ChatbotController {

    private final ChatbotService chatbotService;

    @Autowired
    public ChatbotController(ChatbotService chatbotService) {
        this.chatbotService = chatbotService;
    }

    @PostMapping("/price")
    public Map<String, Object> price(@Nullable @RequestBody Map<String, Object> body){
        log.info("body {}", body);



        Map<String, Object> userinfoMap =  (Map<String, Object>)body.get("userInfo");
        Map<String, Object> entities = (Map<String, Object>)userinfoMap.get("entities");
        String productName =  (String)entities.get("`@SYSTEM_ANY");

        List<Chatbot> searchList = chatbotService.price(productName);

        log.info("searchList: {}", searchList);


        Map<String, Object> rs = new HashMap<>();
        Map<String, Object> list = new HashMap<>();
        list.put("variableName","list");
        list.put("value", searchList.toString());


        ArrayList<Map<String, Object>> dataList = new ArrayList<>();
        dataList.add(list);

        rs.put("data", dataList);
        log.info("rs {}", rs);
        return rs;
    }

    @GetMapping("/chatbot")
    public String chatbot(String msg) {
        return NaverCloud.ChatBot(msg);
    }

}
