package com.goldensnitch.qudditch.controller;

import com.goldensnitch.qudditch.dto.QrAccessReq;
import com.goldensnitch.qudditch.dto.StoreVisitorLog;
import com.goldensnitch.qudditch.service.QrcodeAccessService;
import com.goldensnitch.qudditch.service.RedisService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/api/access/qrcode")
public class QrcodeAccessController {
    private final QrcodeAccessService qrcodeAccessService;


    public QrcodeAccessController(QrcodeAccessService qrcodeAccessService, RedisService redisService) {
        this.qrcodeAccessService = qrcodeAccessService;
    }


    @PostMapping("/request")
    public String requestQrAccess(){
        // TODO : 로그인한 사용자 정보(customer)를 가져와서 request에 넣어줘야함
        QrAccessReq request = new QrAccessReq();
        request.setUserId(1);
//        request.setUserId(Integer.parseInt(SecurityContextHolder.getContext().getAuthentication().getName()));
        return qrcodeAccessService.requestQrAccess(request);

    }

    @GetMapping("/confirm")
    public String confirm(String uuid) {
        // TODO : 로그인한 사용자(store) 정보를 가져와서 request에 넣어줘야함
        StoreVisitorLog storeVisitorLog = new StoreVisitorLog();
//        storeVisitorLog.setUserStoreId((Integer) SecurityContextHolder.getContext().getAuthentication().getCredentials());
        storeVisitorLog.setUserStoreId(2);
        return qrcodeAccessService.confirmQrAccess(uuid, storeVisitorLog);

    }

}
