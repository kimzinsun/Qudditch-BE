package com.goldensnitch.qudditch.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.goldensnitch.qudditch.dto.Chatbot;
import com.goldensnitch.qudditch.dto.ChatbotDto;
import com.goldensnitch.qudditch.dto.Store;
import com.goldensnitch.qudditch.naver.NaverCloud;
import com.goldensnitch.qudditch.service.ChatbotService;
import com.goldensnitch.qudditch.service.ExtendedUserDetails;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
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
    public Map<String, Object> price(@Nullable @RequestBody Map<String, Object> body) throws JsonProcessingException {
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

        /*StringBuilder sb = new StringBuilder();
        for (Chatbot product : searchList) {
            sb.append(product.getName()).append(": ").append(product.getPrice()).append("원\n");
        }*/

        ObjectMapper objectMapper = new ObjectMapper();
        String jsonStr = objectMapper.writeValueAsString(Map.of("type","price","data",searchList));

        log.info("rs {}", rs);
        list.put("value", "'"+jsonStr+"'");
        ls.add(list);
        rs.put("data", ls);
        return rs;
    }


    /*검색한 상품이 있는 사용자의 근처 편의점 추출*/
    @PostMapping("/store")
    public Map<String, Object> store(@RequestBody Map<String, Object> body){
        log.info("body {}", body);
        Map<String, Object> userinfoMap =  (Map<String, Object>)body.get("userInfo");
        Map<String, Object> entities = (Map<String, Object>)userinfoMap.get("entities");
        String productName =  (String)entities.get("`@SYSTEM_ANY");

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
    public String chatbot(@AuthenticationPrincipal ExtendedUserDetails userDetails, ChatbotDto dto) throws JsonProcessingException {
        String returnValue = "";
        String rtnStr = NaverCloud.ChatBot(dto.getMsg(), userDetails.getId());
        JSONObject jsonObject = new JSONObject(rtnStr);
        String rtnMsg = (String)jsonObject.getJSONArray("bubbles")
                .getJSONObject(0)
                .getJSONObject("data")
                .get("description");
        ObjectMapper objectMapper = new ObjectMapper();
        // 커스텀 쿼리하는 부분, 액션메서드 결과값으로 DB에 select하는 경우
        if(rtnMsg.startsWith("msg:")){
            String pattern = "msg:(\\w+)/"; // 정규 표현식 패턴

            Pattern p = Pattern.compile(pattern);
            Matcher msgRex = p.matcher(rtnMsg);

            // 일치하는 부분을 찾습니다.
            if (msgRex.find()) {
                String msgCode = msgRex.group(1); // 첫 번째 그룹을 가져와서 결과로 출력합니다.
                log.info(msgCode);

                if(msgCode.equals("select_001")){

                    String productNameRexPattern = "productName:(.*?)$"; // 정규 표현식 패턴
                    p = Pattern.compile(productNameRexPattern);
                    Matcher productNameRex = p.matcher(rtnMsg);

                    if (productNameRex.find()) {
                        List<Object> select001 = new ArrayList<>();
                        String productName = productNameRex.group(1); // 첫 번째 그룹을 가져와서 결과로 출력합니다.
                        log.info(productName);

                        List<String> similarProductNames = chatbotService.getSimilarProductNameList(productName);

                        for(String pName : similarProductNames){
                            List<Store> stores = chatbotService.getNearProductStoreList(pName, dto.getCurrentWgs84X(), dto.getCurrentWgs84Y());
                            select001.add(Map.of("name",pName,"stores", stores));
                        }

                        returnValue = '"'+objectMapper.writeValueAsString(Map.of("type", "select001", "data", select001))+'"';
                    } else {
                        System.out.println("일치하는 패턴을 찾을 수 없습니다.");
                        returnValue = "Fail";
                    }
                } else if(msgCode.equals("select_002")){
                    List<Store> nearStores = chatbotService.getNearStoreList(dto.getCurrentWgs84X(), dto.getCurrentWgs84Y());
                    returnValue = '"'+objectMapper.writeValueAsString(Map.of("type", "select002", "data", nearStores))+'"';
                }

            } else {
                log.info("일치하는 패턴을 찾을 수 없습니다.");
                returnValue = "Fail";
            }
        }else {
            returnValue = rtnMsg;
        }

        return returnValue;
    }


    /*랜덤으로 상품추천해주는 액션메소드*/
    @PostMapping(path = "/random", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public Map<String, Object> random(@Nullable @RequestBody Map<String, Object> body) throws JsonProcessingException {

        log.info("body {}", body);

        List<Chatbot> randomList = chatbotService.random();

        log.info("randomList: {}", randomList);

        Map<String, Object> ra = new HashMap<>();
        ArrayList<Map<String, Object>> rs = new ArrayList<>();
        Map<String, Object> recommend = new HashMap<>();
        recommend.put("variableName","recommend");

        /*StringBuilder sb = new StringBuilder();
        for (Chatbot random : randomList) {
            sb.append(random.getName()).append(" : ").append(random.getPrice()).append("원\n");
        }
*/
        ObjectMapper objectMapper = new ObjectMapper();
        String jsonStr = objectMapper.writeValueAsString(Map.of("type","random","data",randomList));

        recommend.put("value", "'"+jsonStr+"'");
        rs.add(recommend);
        ra.put("data", rs);
        return ra;
    }

    @PostMapping(path = "/best", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public Map<String, Object> best(@Nullable @RequestBody Map<String, Object> body) throws JsonProcessingException {
        log.info("body {}", body);

        List<Chatbot> bestList = chatbotService.best();

        log.info("bestList: {}", bestList);

        Map<String, Object> ba = new HashMap<>();
        ArrayList<Map<String, Object>> bs = new ArrayList<>();
        Map<String, Object> product = new HashMap<>();
        product.put("variableName","product");

        /*StringBuilder sr = new StringBuilder();
        for (Chatbot best : bestList) {
            sr.append(best.getName()).append(" : ").append(best.getPrice()).append("원\n");
        }*/

        ObjectMapper objectMapper = new ObjectMapper();
        String jsonStr = objectMapper.writeValueAsString(Map.of("type","best","data",bestList));
        System.out.println(jsonStr);
        product.put("value", "'"+jsonStr+"'");
        bs.add(product);
        ba.put("data", bs);

        return ba;
    }

}
