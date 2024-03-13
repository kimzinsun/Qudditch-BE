package com.goldensnitch.qudditch.service;

import com.goldensnitch.qudditch.dto.QrAccessReq;
import com.goldensnitch.qudditch.dto.StoreVisitorLog;
import com.goldensnitch.qudditch.mapper.AccessMapper;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.Objects;
import java.util.UUID;

@Service
public class QrcodeAccessService {
    private final RedisService redisService;
    private final AccessMapper accessMapper;

    public QrcodeAccessService(RedisService redisService, AccessMapper accessMapper) {
        this.redisService = redisService;
        this.accessMapper = accessMapper;
    }


    public void requestQrAccess(QrAccessReq request) {
        String uuid = UUID.randomUUID().toString();
        System.out.println(uuid);
        redisService.setValues(uuid, String.valueOf(request.getUserId()), Duration.ofSeconds(30));
    }

    public boolean confirmQrAccess(String uuid, StoreVisitorLog storeVisitorLog) {

        if (Objects.equals(redisService.getValues(uuid), "false")) {
            System.out.println("QR코드 접근이 허용되지 않았습니다.");
            return false;
        } else {
            int userId = Integer.parseInt(redisService.getValues(uuid));
            storeVisitorLog.setUserCustomerId(userId);
            System.out.println(storeVisitorLog);
            accessMapper.insertVisitLog(storeVisitorLog);
            redisService.deleteValues(uuid);

            System.out.println("QR코드 접근이 허용되었습니다.");
            return true;
        }


    }
}
