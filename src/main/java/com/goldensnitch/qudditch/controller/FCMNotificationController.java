package com.goldensnitch.qudditch.controller;

import com.goldensnitch.qudditch.dto.CustomerDevice;
import com.goldensnitch.qudditch.dto.UserCustomer;
import com.goldensnitch.qudditch.service.FCMNotificationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/api/fcm")
public class FCMNotificationController {

    @Autowired
    FCMNotificationService service;

    @PostMapping("/login-device")
    public ResponseEntity<String> loginDevice(@RequestBody Map<String, String> requestBody){
        String email = requestBody.get("email");
        String deviceToken = requestBody.get("deviceToken");

        log.info("FCMNotificationController.loginDevice: email {}, deviceToken {}", email, deviceToken);

        UserCustomer userCustomer = service.getUserCustomerByEmail(email);

        CustomerDevice customerDevice = new CustomerDevice();
        customerDevice.setUserCustomerId(userCustomer.getId());
        customerDevice.setToken(deviceToken);

        service.registerCustomerDevice(customerDevice);

        return ResponseEntity.ok("SUCCESS");
    }

    @DeleteMapping("/logout-device")
    public ResponseEntity<String> logoutDevice(@RequestBody Map<String, String> requestBody){
        String email =  requestBody.get("email");

        log.info("FCMNotificationController.loginDevice: email {}", email);

        UserCustomer userCustomer = service.getUserCustomerByEmail(email);

        boolean isSuccess = service.RemoveCustomerDevice(userCustomer.getId());

        if (isSuccess){
            return ResponseEntity.ok("SUCCESS");
        } else {
            return ResponseEntity.ok("email:" + email + " 디바이스 토큰이 삭제되지 않았습니다.");
        }
    }

}
