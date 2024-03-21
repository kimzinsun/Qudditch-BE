package com.goldensnitch.qudditch.controller;

import com.goldensnitch.qudditch.dto.CustomerBookmarkProduct;
import com.goldensnitch.qudditch.dto.CustomerDevice;
import com.goldensnitch.qudditch.dto.UserCustomer;
import com.goldensnitch.qudditch.service.FCMNotificationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/api/fcm")
public class FCMNotificationController {

    @Autowired
    FCMNotificationService service;

    @GetMapping("/test")
    public String test(){
        return "test success";
    }

    @PostMapping("/loginDevice")
    public String loginDevice(@RequestBody Map<String, String> deviceDto){
        log.info("FCMNotificationController.loginDevice: email {}, deviceToken {}", deviceDto.get("email"), deviceDto.get("deviceToken"));

        String email = deviceDto.get("email");
        String deviceToken = deviceDto.get("deviceToken");

        UserCustomer userCustomer = service.getUserCustomerByEmail(email);

        CustomerDevice customerDevice = new CustomerDevice();
        customerDevice.setUserCustomerId(userCustomer.getId());
        customerDevice.setToken(deviceToken);

        return service.registerCustomerDevice(customerDevice) ? "SUCCESS" : "FAIL";
    }

    @DeleteMapping("/logoutDevice")
    public String logoutDevice(String email){
        log.info("FCMNotificationController.loginDevice: email {}", email);

        UserCustomer userCustomer = service.getUserCustomerByEmail(email);

        return service.RemoveCustomerDevice(userCustomer.getId()) ? "SUCCESS" : "FAIL";
    }

}
