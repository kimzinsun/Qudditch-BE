package com.goldensnitch.qudditch.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
@RequestMapping("/api/store")
public class StoreStockController {
    // TODO : store 관련 기능 구현

    @GetMapping("/stock")
    public ResponseEntity getStockList() {

        return ResponseEntity.ok().build();
    }


}
