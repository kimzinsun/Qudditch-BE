package com.goldensnitch.qudditch.service;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class TestService {
    // @Value("${spring.security.oauth2.client.registration.kakao.redirect-uri}")
    private String kakaoApiKey = "05b583074e45542f4c8ff3f86f5ef0f5";
    // @Value("${spring.security.oauth2.client.registration.kakao.client-id}")
    private String kakaoRedirectUri ="http://localhost:3000/mobile/login/kakao";

    public String getAccessToken(String code) {
        String accessToken = "";
        String refreshToken = "";
        String reqUrl = "https://kauth.kakao.com/oauth/token";

        try {
            URL url = new URL(reqUrl);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();

            //필수 헤더 세팅
            conn.setRequestProperty("Content-type", "application/x-www-form-urlencoded;charset=utf-8");
            conn.setDoOutput(true); //OutputStream으로 POST 데이터를 넘겨주겠다는 옵션.

            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(conn.getOutputStream()));

            //필수 쿼리 파라미터 세팅
            String params = "grant_type=authorization_code" +
                        "&client_id=" + kakaoApiKey +
                        "&redirect_uri=" + kakaoRedirectUri +
                        "&client_secret=" + "vLtb75wHsNpMDC4t6uiZjpluEC4HoITH" +
                        "&code=" + code;

            bw.write(params);
            bw.flush();
            bw.close();

            int responseCode = conn.getResponseCode();
            log.info("[KakaoApi.getAccessToken] responseCode = {}", responseCode);

            BufferedReader br = new BufferedReader(new InputStreamReader(
                responseCode == 200 ? conn.getInputStream() : conn.getErrorStream()));

                StringBuilder responseSb = new StringBuilder();
                String line;
                while ((line = br.readLine()) != null) {
                    responseSb.append(line);
                }
                br.close();
    
                if (responseCode != 200) {
                    log.error("Failed to retrieve access token: {}", responseSb.toString());
                    return null; // 혹은 적절한 예외를 던지기
                }

                JsonParser parser = new JsonParser();
                JsonElement element = parser.parse(responseSb.toString());
                accessToken = element.getAsJsonObject().get("access_token").getAsString();
                refreshToken = element.getAsJsonObject().get("refresh_token").getAsString();
    
            } catch (Exception e) {
                log.error("Exception occurred while getting access token", e);
            }
            return accessToken;
        }

    public HashMap<String, Object> getUserInfo(String accessToken) {
        HashMap<String, Object> userInfo = new HashMap<>();
        String reqUrl = "https://kapi.kakao.com/v2/user/me";
        try {
            URL url = new URL(reqUrl);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Authorization", "Bearer " + accessToken);
            conn.setRequestProperty("Content-type", "application/x-www-form-urlencoded;charset=utf-8");

            int responseCode = conn.getResponseCode();
            log.info("[KakaoApi.getUserInfo] responseCode : {}", responseCode);

            BufferedReader br = new BufferedReader(new InputStreamReader(
                responseCode == 200 ? conn.getInputStream() : conn.getErrorStream()));

            StringBuilder responseSb = new StringBuilder();
            String line;
            while ((line = br.readLine()) != null) {
                responseSb.append(line);
            }
            br.close();

            if (responseCode != 200) {
                log.error("Failed to retrieve user info: {}", responseSb.toString());
                return null; // 혹은 적절한 예외를 던지기
            }

           ObjectMapper objectMapper = new ObjectMapper();
           Map<String, Object> responseMap = objectMapper.readValue(responseSb.toString(), Map.class);
            System.out.println(responseMap);
            System.out.println(responseMap.get("id"));
            Map<String, Object> kakaoAccount = objectMapper.convertValue(responseMap.get("kakao_account"), Map.class);
           
           System.out.println(kakaoAccount);
           userInfo.put("email", kakaoAccount.get("email"));
           userInfo.put("name", kakaoAccount.get("name"));
            System.out.println(userInfo);
        } catch (Exception e) {
            log.error("Exception occurred while getting user info", e);
        }
        return userInfo;
    }

    public void kakaoLogout(String accessToken) {
        String reqUrl = "https://kapi.kakao.com/v1/user/logout";

        try {
            URL url = new URL(reqUrl);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Authorization", "Bearer " + accessToken);

            int responseCode = conn.getResponseCode();
            log.info("[KakaoApi.kakaoLogout] responseCode : {}", responseCode);

            BufferedReader br = new BufferedReader(new InputStreamReader(
                responseCode == 200 ? conn.getInputStream() : conn.getErrorStream()));

            StringBuilder responseSb = new StringBuilder();
            String line;
            while ((line = br.readLine()) != null) {
                responseSb.append(line);
            }
            br.close();

            if (responseCode != 200) {
                log.error("Failed to logout: {}", responseSb.toString());
            } else {
                log.info("kakao logout - responseBody = {}", responseSb.toString());
            }
        } catch (Exception e) {
            log.error("Exception occurred while logging out", e);
        }
    }
}