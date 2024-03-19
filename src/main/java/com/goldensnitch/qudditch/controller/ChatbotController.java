package com.goldensnitch.qudditch.controller;

import com.goldensnitch.qudditch.dto.Chatbot;
import com.goldensnitch.qudditch.naver.NaverCloud;
import com.goldensnitch.qudditch.service.ChatbotService;
import com.nimbusds.jose.shaded.gson.Gson;
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

        /*        Map<String,Object>rs=new HashMap<>();
        Map<String,Object>name=new HashMap<>();
        Map<String,Object>price=new HashMap<>();
        name.put("variableName","name"); // 답변에서 $2{price.name}처럼 접근할 키명 액센 메서드 이름이 price이므로
        name.put("value","test"); // 답변에서 $2{price.name}할때 나올 값
        price.put("variableName","price"); // 답변에서 $2{price.price} 키
        price.put("value","1000"); // 답변에서 $2{price.price} 값*/


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

    @PostMapping("/store")
    public String store(@Nullable @RequestBody Map<String, Object> body) {

        log.info("body {}", body);

        Map<String, Object> userinfo =  (Map<String, Object>)body.get("userInfo");
        Map<String, Object> entities = (Map<String, Object>)userinfo.get("entities");
        String storeName =  (String)entities.get("`@SYSTEM_ANY");

        List<Chatbot> storeSearchList = chatbotService.store(storeName);

        log.info("storeSearchList: {}", storeSearchList);

        Map<String, Object> sr = new HashMap<>();
        Map<String, Object> list = new HashMap<>();
        list.put("variableName","list");
        list.put("value", storeSearchList.toString());

        ArrayList<Map<String, Object>> dataList = new ArrayList<>();
        dataList.add(list);

        sr.put("data", dataList);
        log.info("rs {}", sr);
        Gson gson = new Gson();
        return gson.toJson(sr);
    }


    @GetMapping("/chatbot")
    public String chatbot(String msg) {
        return NaverCloud.ChatBot(msg);
    }

}
