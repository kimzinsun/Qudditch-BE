package com.goldensnitch.qudditch.service;

import com.goldensnitch.qudditch.dto.CustomerAlertLog;
import com.goldensnitch.qudditch.dto.CustomerDevice;
import com.goldensnitch.qudditch.dto.fcm.FCMNotificationRequestDto;
import com.goldensnitch.qudditch.mapper.FCMMapper;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingException;
import com.google.firebase.messaging.Message;
import com.google.firebase.messaging.Notification;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class FCMNotificationService {

    private final FirebaseMessaging firebaseMessaging;

    @Autowired
    private FCMMapper mapper;

    public boolean registerCustomerDevice(CustomerDevice dto){
        int result = -1;

        boolean isNotDuplicate = mapper.countCustomerDevice(dto.getUserCustomerId()) == 0;

        if(isNotDuplicate){
            result = mapper.insertCustomerDevice(dto);
        }else{
            result = mapper.updateCustomerDeviceToken(dto);
        }

        return result > 0;
    }

    public String sendNotificationByToken(int userCustomerId, FCMNotificationRequestDto requestDto) {
        CustomerDevice customerDevice = mapper.selectCustomerDevice(userCustomerId);
        String token = customerDevice.getToken();

        if (customerDevice != null) {
            if (token!= null && !token.isEmpty()) {
                Notification notification = Notification.builder()
                        .setTitle(requestDto.getTitle())
                        .setBody(requestDto.getBody())
                        .build();

                Message message = Message.builder()
                        .setToken(token)
                        //.setToken("fBoToRZwQ6SCRtnYaQ0Zmh:APA91bGSA1oFep7_UHFICfZvZcgkQiAPntGcoYi9zMtrKAD9jBjqVBWld_w2p3nd_fOWyUbrBH_-etOp67sIho8PfOyQiyDlKnHMqnIrF9yU93U2f8cs67YcyKaxIgfilvOMEFl3M4FR")
                        .setNotification(notification)
                        // .putAllData(requestDto.getData())
                        .build();

                try {
                    firebaseMessaging.send(message);

                    CustomerAlertLog alertLog = new CustomerAlertLog();
                    alertLog.setUserCustomerId(userCustomerId);
                    alertLog.setTitle(requestDto.getTitle());
                    alertLog.setBody(requestDto.getBody());

                    mapper.insertCustomerAlertLog(alertLog);

                    return "알림을 성공적으로 전송했습니다. targetUserId=" + requestDto.getTargetUserId();
                } catch (FirebaseMessagingException e) {
                    e.printStackTrace();
                    return "알림 보내기를 실패하였습니다. targetUserId=" + requestDto.getTargetUserId();
                }
            } else {
                return "서버에 저장된 해당 유저의 FirebaseToken이 존재하지 않습니다. targetUserId=" + requestDto.getTargetUserId();
            }

        } else {
            return "해당 유저가 존재하지 않습니다. targetUserId=" + requestDto.getTargetUserId();
        }
    }

    public List<CustomerAlertLog> getCustomerAlertLogs(int userCustomerId){
        return mapper.selectCustomerAlertLogs(userCustomerId);
    }
}