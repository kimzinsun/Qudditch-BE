package com.goldensnitch.qudditch.controller;

import com.goldensnitch.qudditch.dto.Chatbot;
import com.goldensnitch.qudditch.dto.ChatbotDto;
import com.goldensnitch.qudditch.dto.Store;
import com.goldensnitch.qudditch.naver.NaverCloud;
import com.goldensnitch.qudditch.service.ChatbotService;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Slf4j
@RestController
@RequestMapping("/api/chatbot")
public class ChatbotController {

    private final ChatbotService chatbotService;

    @Autowired
    public ChatbotController(ChatbotService chatbotService) {
        this.chatbotService = chatbotService;
    }

    // 상품의 가격을 물어보면 그 상품의 리스트와 가격을 출력
    @PostMapping(path = "/price", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
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
        ArrayList<Map<String, Object>> ls = new ArrayList<>();
        Map<String, Object> list= new HashMap<>();
        list.put("variableName","list");
//        list.put("value", searchList.toString());

        StringBuilder sb = new StringBuilder();
        for (Chatbot product : searchList) {
            sb.append(product.getName()).append(": ").append(product.getPrice()).append("원\n");
        }

        log.info("rs {}", rs);
        list.put("value", sb.toString());
        ls.add(list);
        rs.put("data", ls);
        return rs;
    }

    /* // 특정 상품이 있는 가게 리스트 출력
    @PostMapping(path = "/store", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public Map<String, Object> store(@Nullable @RequestBody Map<String, Object> body) {

        log.info("body {}", body);

        Map<String, Object> userinfo =  (Map<String, Object>)body.get("userInfo");
        Map<String, Object> entities = (Map<String, Object>)userinfo.get("entities");
        String storeName =  (String)entities.get("`@SYSTEM_ANY");

        List<ChatbotStore> storeSearchList = chatbotService.store(storeName);

        log.info("storeSearchList: {}", storeSearchList);

        Map<String, Object> sr= new HashMap<>();
        ArrayList<Map<String, Object>> ls = new ArrayList<>();
        Map<String, Object> list = new HashMap<>();
        list.put("variableName","list");

        StringBuilder sb = new StringBuilder();
        for (ChatbotStore name : storeSearchList) {
            sb.append(name.getName()).append("\n");
        }

        list.put("value", sb.toString());
        ls.add(list);
        sr.put("data", ls);
        return sr;
    }*/

    /*검색한 상품이 있는 사용자의 근처 편의점 추출*/
    @PostMapping("/store")
    public Map<String, Object> store(@RequestBody Map<String, Object> body){
        log.info("body {}", body);
        Map<String, Object> userinfoMap =  (Map<String, Object>)body.get("userInfo");
        Map<String, Object> entities = (Map<String, Object>)userinfoMap.get("entities");
        String productName =  (String)entities.get("`@SYSTEM_ANY");

        // System.out.println("productName:" + productName);

        Map<String,Object>rs=new HashMap<>();
        Map<String,Object>productNameMap=new HashMap<>();
        productNameMap.put("variableName","productName");
        productNameMap.put("value",productName);

        ArrayList<Map<String,Object>>dataList= new ArrayList<>();
        dataList.add(productNameMap);

        rs.put("data",dataList); // data키에 각종 속성을 리스트로 담아야함
        log.info("rs{}",rs);
        return rs;
    }

    /*기본 챗봇연동 및 질문 분류*/
    @GetMapping("/chatbot")
    public String chatbot(ChatbotDto dto){
        String returnValue = "";
        String rtnStr = NaverCloud.ChatBot(dto.getMsg());
        JSONObject jsonObject = new JSONObject(rtnStr);
        String rtnMsg = (String)jsonObject.getJSONArray("bubbles")
                .getJSONObject(0)
                .getJSONObject("data")
                .get("description");

        // 커스텀 쿼리하는 부분, 액션메서드 결과값으로 DB에 select하는 경우
        if(rtnMsg.startsWith("msg:")){
            String pattern = "msg:(\\w+)/"; // 정규 표현식 패턴

            Pattern p = Pattern.compile(pattern);
            Matcher msgRex = p.matcher(rtnMsg);

            // 일치하는 부분을 찾습니다.
            if (msgRex.find()) {
                String msgCode = msgRex.group(1); // 첫 번째 그룹을 가져와서 결과로 출력합니다.
                System.out.println(msgCode);

                if(msgCode.equals("select_001")){

                    String productNameRexPattern = "productName:(.*?)$"; // 정규 표현식 패턴
                    p = Pattern.compile(productNameRexPattern);
                    Matcher productNameRex = p.matcher(rtnMsg);

                    if (productNameRex.find()) {
                        String productName = productNameRex.group(1); // 첫 번째 그룹을 가져와서 결과로 출력합니다.
                        System.out.println(productName);

                        List<String> similarProductNames = chatbotService.getSimilarProductNameList(productName);

                        StringBuilder sb = new StringBuilder();

                        for(String pName : similarProductNames){
                            List<Store> stores = chatbotService.getNearProductStoreList(pName, dto.getCurrentWgs84X(), dto.getCurrentWgs84Y());

                            if(stores.size() == 0){
                                sb.append(pName + " 제품이 존재하는 가게를 찾을 수 없습니다.\n");
                            }else{
                                sb.append(pName + "제품이 있는 가게중 현재위치에서 가까운 가게는 아래와 같습니다.\n");

                                for(Store store : stores){
                                    sb.append("\t"+ store.getName() + "의 주소는 " + store.getAddress() + "\n");
                                }
                            }
                        }

                        returnValue = sb.toString();
                    } else {
                        System.out.println("일치하는 패턴을 찾을 수 없습니다.");
                        returnValue = "Fail";
                    }
                } else if(msgCode.equals("select_002")){
                    List<Store> nearStores = chatbotService.getNearStoreList(dto.getCurrentWgs84X(), dto.getCurrentWgs84Y());

                    StringBuilder sb = new StringBuilder();

                    for(Store store : nearStores){
                        sb.append("가게이름: " + store.getName() + ", 주소: " + store.getAddress()).append("\n");
                    }

                    returnValue = sb.toString();
                }

            } else {
                System.out.println("일치하는 패턴을 찾을 수 없습니다.");
                returnValue = "Fail";
            }
        }else {
            returnValue = rtnMsg;
        }

        return returnValue;
    }

    /*@GetMapping("/chatbot")
    public String chatbot(String msg) {
        return NaverCloud.ChatBot(msg);
    }*/

    /*랜덤으로 상품추천해주는 액션메소드*/
    @PostMapping(path = "/random", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public Map<String, Object> random(@Nullable @RequestBody Map<String, Object> body) {

        log.info("body {}", body);

        List<Chatbot> randomList = chatbotService.random();

        log.info("randomList: {}", randomList);

        Map<String, Object> ra = new HashMap<>();
        ArrayList<Map<String, Object>> rs = new ArrayList<>();
        Map<String, Object> recommend = new HashMap<>();
        recommend.put("variableName","recommend");

        StringBuilder sb = new StringBuilder();
        for (Chatbot random : randomList) {
            sb.append(random.getName()).append(" : ").append(random.getPrice()).append("원\n");;
        }

        recommend.put("value", sb.toString());
        rs.add(recommend);
        ra.put("data", rs);
        return ra;
    }

}
