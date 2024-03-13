package com.goldensnitch.qudditch.controller;

import com.goldensnitch.qudditch.dto.manage.InputReq;
import com.goldensnitch.qudditch.dto.manage.OrderDetailRes;
import com.goldensnitch.qudditch.dto.manage.OrderRes;
import com.goldensnitch.qudditch.service.ManageService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/manage")
public class ManageController {
    private final ManageService manageService;

    public ManageController(ManageService manageService) {
        this.manageService = manageService;
    }

    @GetMapping("/order")
    public List<OrderRes> getOrderList() {
        return manageService.getOrderList();
    }

    @GetMapping("/order/detail/{orderStoreId}")
    public List<OrderDetailRes> getOrderDetail(@PathVariable int orderStoreId) {
        return manageService.getOrderDetail(orderStoreId);
    }

    @PostMapping("/order/detail/{orderStoreId}")
    public void confirmOrder(@PathVariable int orderStoreId, @RequestBody List<InputReq> list) {
        manageService.confirmOrder(orderStoreId, list);
    }

}
