package com.goldensnitch.qudditch.controller;

import com.goldensnitch.qudditch.dto.CustomerAlertLog;
import com.goldensnitch.qudditch.dto.CustomerDevice;
import com.goldensnitch.qudditch.dto.UserCustomer;
import com.goldensnitch.qudditch.service.ExtendedUserDetails;
import com.goldensnitch.qudditch.service.FCMNotificationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
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

        UserCustomer userCustomer = service.getUserCustomerByEmail(email);

        CustomerDevice customerDevice = new CustomerDevice();
        customerDevice.setUserCustomerId(userCustomer.getId());
        customerDevice.setToken(deviceToken);

        service.registerCustomerDevice(customerDevice);

        return ResponseEntity.ok("SUCCESS");
    }

    @PutMapping("/logout-device")
    public ResponseEntity<String> logoutDevice(@RequestBody Map<String, String> requestBody){
        String email =  requestBody.get("email");

        UserCustomer userCustomer = service.getUserCustomerByEmail(email);

        boolean isSuccess = service.loggedOutCustomerDevie(userCustomer.getId());

        if (isSuccess){
            return ResponseEntity.ok("SUCCESS");
        } else {
            return ResponseEntity.ok("email:" + email + " 디바이스 토큰이 로그아웃 상태로 변경되지 않았습니다.");
        }
    }

    @GetMapping("/customer-device")
    public ResponseEntity<CustomerDevice> getCustomerDevice(@AuthenticationPrincipal ExtendedUserDetails userDetails){
        return ResponseEntity.ok(service.getCustomerDevice(userDetails.getId()));
    }

    @PostMapping("/notification")
    public ResponseEntity<String> setNotificationOnOff(@AuthenticationPrincipal ExtendedUserDetails userDetails, boolean active){
        CustomerDevice customerDevice = new CustomerDevice();
        customerDevice.setUserCustomerId(userDetails.getId());
        customerDevice.setState(active);

        service.setNotificationOnOff(customerDevice);

        return ResponseEntity.ok("SUCCESS");
    }

    @GetMapping("/alerts")
    public ResponseEntity<List<CustomerAlertLog>> getAllAlerts(@AuthenticationPrincipal ExtendedUserDetails userDetails){
        int id = userDetails.getId();

        return ResponseEntity.ok(service.getCustomerAlertLogs(id));
    }

    @DeleteMapping("/alert")
    public ResponseEntity<String> removeAlert(@AuthenticationPrincipal ExtendedUserDetails userDetails, @RequestBody Map<String,Integer> body){
        String userName = userDetails.getUsername();
        int alertId = body.get("alertId");
        boolean isSuccess = service.removeCustomerAlertLog(alertId);

        if (isSuccess){
            return ResponseEntity.ok("SUCCESS");
        } else {
            return ResponseEntity.ok("user:" + userName + " 디바이스 토큰이 삭제되지 않았습니다.");
        }
    }
}
